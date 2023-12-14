ARG JAVA_VERSION="21"
FROM amazoncorretto:${JAVA_VERSION}

RUN yum install -y shadow-utils tar

# Create the user
ARG USER_NAME=vscode
ARG USER_UID=1000
ARG USER_GID=$USER_UID
ARG HOME_DIR=/home/${USER_NAME}
RUN groupadd --gid $USER_GID $USER_NAME \
    && useradd --uid $USER_UID --gid $USER_GID -m $USER_NAME \
    #
    # [Optional] Add sudo support. Omit if you don't need to install software after connecting.
    && yum update -y \
    && yum install -y sudo \
    && echo $USER_NAME ALL=\(root\) NOPASSWD:ALL > /etc/sudoers.d/$USER_NAME \
    && chmod 0440 /etc/sudoers.d/$USER_NAME

# Git
RUN yum install -y git

# SSH
# https://code.visualstudio.com/remote/advancedcontainers/sharing-git-credentials
# https://okash1n.works/posts/how-to-use-git-inside-vscode-dev-container/
RUN yum install -y openssh-server
RUN yum install -y openssh-clients
RUN { \
        echo ''; \
        echo '# Launch ssh-agent'; \
        echo 'if [ -z "$SSH_AUTH_SOCK" ]; then'; \
        echo '   # Check for a currently running instance of the agent'; \
        echo '   RUNNING_AGENT="`ps -ax | grep '"'"'ssh-agent -s'"'"' | grep -v grep | wc -l | tr -d '"'"'[:space:]'"'"'`"'; \
        echo '   if [ "$RUNNING_AGENT" = "0" ]; then'; \
        echo '        # Launch a new instance of the agent'; \
        echo '        ssh-agent -s &> $HOME/.ssh/ssh-agent'; \
        echo '   fi'; \
        echo '   eval `cat $HOME/.ssh/ssh-agent`'; \
        echo 'fi'; \
    } >> $HOME_DIR/.bash_profile

# [Optional] Set the default user. Omit if you want to keep the default as root.
USER $USER_NAME
