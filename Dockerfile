# Use a base image with an operating system
FROM ubuntu:latest

COPY src/main/resources/ai4life /home/

# Install necessary packages
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk wget && \
    rm -rf /var/lib/apt/lists/*

# Install Anaconda
RUN wget --no-check-certificate https://repo.anaconda.com/archive/Anaconda3-2020.11-Linux-x86_64.sh && \
    /bin/bash Anaconda3-2020.11-Linux-x86_64.sh -b -p /opt/anaconda3 && \
    rm Anaconda3-2020.11-Linux-x86_64.sh

# Set environment variables
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH="/opt/anaconda3/bin:${PATH}"

# Install Mamba
RUN /opt/anaconda3/bin/conda install -y mamba -c conda-forge

# Create a new environment and install packages using Mamba
RUN /opt/anaconda3/bin/mamba create -n myenv -c simpleitk -c bioconda -c conda-forge/label/cf202003 -c intel -c sebp -c anaconda -c conda-forge -c defaults \
    keras=2.3.1 \
    jupyter=1.0.0 \
    matplotlib=3.3.4 \
    medpy=0.4.0 \
    nibabel=3.2.1 \
    numpy=1.19.2 \
    opencv=3.3.1 \
    pandas=1.1.3 \
    pydicom=1.2.0 \
    pydot=1.4.1 \
    python=3.6.13 \
    scikit-image=0.17.2 \
    scikit-learn=0.23.2 \
    scikit-survival=0.14.0 \
    scipy=1.5.2 \
    seaborn=0.11.1 \
    simpleitk=2.0.2 \
    tensorboard=2.4.0 \
    tensorflow=2.1.0 \
    tensorflow-gpu=2.1.0 \
    tqdm=4.28.1 \
    xlrd=1.2.0 \
    zipp=3.4.1 \
    typing

# Activate the new environment
RUN echo "source activate myenv" >> ~/.bashrc
SHELL ["/bin/bash", "-c"]



