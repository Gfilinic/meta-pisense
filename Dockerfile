# Use a base image with necessary tools (Ubuntu 20.04 in this example)
FROM ubuntu:20.04

ENV DEBIAN_FRONTEND=noninteractive

# Install necessary dependencies for Yocto build
RUN apt-get update && apt-get install -y \
    gawk wget git-core diffstat unzip texinfo gcc-multilib \
    build-essential chrpath socat libsdl1.2-dev xterm zstd \
    locales tzdata \
    cpio lz4 \
    && rm -rf /var/lib/apt/lists/*

# Arguments for user and group IDs
ARG host_uid=1001
ARG host_gid=1001
ARG host_username=pisense-builder  # Define host_username as an argument

# Create the group and user with specified UID and GID
RUN groupadd -g $host_gid $host_username && \
    useradd -g $host_gid -m -s /bin/bash -u $host_uid $host_username && \
    echo "$host_username ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers

# Set the working directory
WORKDIR /meta-pisense

# Copy the entire project directory into the container
COPY . .

# Initialize and update submodules (assuming you have submodules to initialize)
RUN git config --global \
      url."https://github.com/openembedded/meta-openembedded.git".insteadOf \
      "git://git.openembedded.org/meta-openembedded" \
    && git config --global \
      url."https://github.com/yoctoproject/poky.git".insteadOf \
      "git://git.yoctoproject.org/poky" \
    && git config --global \
      url."https://github.com/agherzan/meta-raspberrypi.git".insteadOf \
      "git://git.yoctoproject.org/meta-raspberrypi" \
    && git config --global \
      url."https://code.qt.io/yocto/meta-qt6.git".insteadOf \
      "git://code.qt.io/yocto/meta-qt6.git" \
    && git submodule update --init --recursive
    
# Set up locale (optional, adjust as needed)
RUN locale-gen en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LC_ALL en_US.UTF-8

# Change ownership of all files to myuser
RUN chown -R $host_username:$host_username .

# Switch to the new user
USER $host_username

CMD ["bash"]
