FROM frolvlad/alpine-glibc:alpine-3.19

ARG CONDA_VERSION="py311_23.5.2-0"
ARG CONDA_SHA256="634d76df5e489c44ade4085552b97bebc786d49245ed1a830022b0b406de5817"
ARG CONDA_DIR="/opt/conda"

ENV PATH="$CONDA_DIR/bin:$PATH"
ENV PYTHONDONTWRITEBYTECODE=1

RUN mkdir /requirements
COPY src/main/resources/ai4elife/environment.yml /requirements

RUN mkdir /home/application
COPY . /home/application

# Install conda
RUN echo "**** install dev packages ****" && \
    apk add --no-cache --virtual .build-dependencies bash ca-certificates wget && \
    \
    echo "**** get Miniconda ****" && \
    mkdir -p "$CONDA_DIR" && \
    echo "Downloading Miniconda..." && \
    wget "http://repo.continuum.io/miniconda/Miniconda3-${CONDA_VERSION}-Linux-x86_64.sh" -O miniconda.sh && \
    echo "$CONDA_SHA256  minls -ailiconda.sh" | sha256sum -c && \
    \
    echo "**** install Miniconda ****" && \
    bash miniconda.sh -f -b -p "$CONDA_DIR" && \
    echo "export PATH=$CONDA_DIR/bin:\$PATH" > /etc/profile.d/conda.sh && \
    \
    echo "**** setup Miniconda ****" && \
    conda update --all --yes && \
    conda config --set auto_update_conda False && \
    \
    echo "**** install additional packages ****" && \
    conda env create -f /requirements/environment.yml && \
    \
    echo "**** cleanup ****" && \
    apk del --purge .build-dependencies && \
    rm -f miniconda.sh && \
    conda clean --all --force-pkgs-dirs --yes && \
    find "$CONDA_DIR" -follow -type f \( -iname '*.a' -o -iname '*.pyc' -o -iname '*.js.map' \) -delete && \
    \
    echo "**** finalize ****" && \
    mkdir -p "$CONDA_DIR/locks" && \
    chmod 777 "$CONDA_DIR/locks"

# Install OpenJDK
RUN apk update && \
    echo "Installing OpenJDK..." && \
    apk add --no-cache openjdk17 && \
    echo "OpenJDK installation complete."

# Set JAVA_HOME environment variable
ENV JAVA_HOME /usr/lib/jvm/java-17-openjdk-amd64

WORKDIR /home/application/src/main/resources/ai4elife


