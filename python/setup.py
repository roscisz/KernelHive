from setuptools import setup, find_packages

setup(
    name = 'kernelhive',
    version = '1.2.7',
    packages = find_packages(),
    description = 'Python libraries of KernelHive - an environment for executing, designing and tuning parallel applications in multi-level heterogeneous HPC systems',
    author = 'Pawel Rosciszewski',
    author_email = 'pawel.rosciszewski@pg.edu.pl',
    url = 'https://github.com/roscisz/KernelHive',
    download_url = 'https://github.com/roscisz/KernelHive/archive/1.2.7.tar.gz',
    keywords = 'parallel cluster monitoring resource management',
    install_requires=['paramiko', 'werkzeug', 'json-rpc']
)
