# KernelHive

**KernelHive** is an open source environment for executing, designing and tuning parallel applications in multi-level heterogeneous high performance computing systems.

Citing KernelHive: Rościszewski, P., Czarnul, P., Lewandowski, R., Schally-Kacprzak, M., 2016. KernelHive: a new workflow-based framework for multilevel high performance computing using clusters and workstations with CPUs and GPUs. Concurrency and Computation: Practice and Experience 28, 2586–2607. doi:10.1002/cpe.3719

BibTeX:
<pre>
@article{rosciszewski_kernelhive_2016,
        title = {{KernelHive}: a new workflow-based framework for multilevel high performance computing using clusters and workstations with {CPUs} and {GPUs}},
        volume = {28},
        issn = {15320626},
        shorttitle = {{KernelHive}},
        url = {http://doi.wiley.com/10.1002/cpe.3719},
        doi = {10.1002/cpe.3719},
        language = {en},
        number = {9},
        journal = {Concurrency and Computation: Practice and Experience},
        author = {Rościszewski, Paweł and Czarnul, Paweł and Lewandowski, Rafał and Schally-Kacprzak, Marcel},
        month = jun,
        year = {2016},
        pages = {2586--2607}
}
</pre>

## KernelHive highlights
- multi-level parallelization
- utilization of heterogeneous computing devices
- flexible parallel application model
- dynamic allocation of computational resources through unrollable nodes
- dynamic actualization of available infrastructure
- distributed data storage and transfer

KernelHive system can be a basis for conducting research on:
- task scheduling algorithms
- fault tolerance mechanisms
- load balancing
- development of parallel applications in various fields

## Getting started

### Prerequisities

In order to compile KernelHive you need to install following packages/applications:
* Nvidia proprietary drivers
* nvidia-opencl-dev
* boost (system, filesystem, thread)
* mongodb (2.6.x)
* mongo-cxx-driver (26compat)
* libssl-dev
* g++
* cmake
* jdk (version 7)
* maven
* glassfish 3.1.2.2

Most of them may be installed from standard Linux packages, but some may need to be downloaded and installed manually. Usually, the following packages need to be installed manually: mongodb, mongo-cxx-driver and glassfish.

### Downloading KernelHive

<pre>
git clone https://github.com/roscisz/KernelHive.git
</pre>

### Building KernelHive

You may build KernelHive either by using provided script or manually.

#### Building using script

To build KernelHive using provided script (khbuilder) run it with option build (or without any options as it defaults to build) from main directory:
<pre>
./khbuilder build
</pre>

Provided script also enables to clean all build output by running it with option clean:
<pre>
./khbuilder clean
</pre>

#### Building manually

Manual build consists of  2 steps: building C++ code and building Java code as described below.

##### Building C++ code

* Create and enter directory `build` in src/
* Build Makefiles using CMake
* Build

<pre>
mkdir build
cd build
cmake ..
make
</pre>

##### Building Java code

Inside src directory run (Compile sources and install them to local maven repository):
<pre>
mvn install
</pre>

### Testing OpenCL environment

When everything is built you can check if OpenCL recognizes compatible devices by running khrun script with devices-info parameter:
<pre>
./khrun devices-info
</pre>

If you don't see your device (GPU or OpenCL compatible CPU) check if you correctly installed your device drivers.

### Running KernelHive

In order to run KernelHive you need to run at least one engine (for job management), one cluster (connects to engine, asks for jobs and assign jobs to units connected to it) and one unit (represents one host in cluster, connects to cluster, waits for jobs and create workers to execute jobs). All of them may be run either by khrun script or manually.

You also need to install and configure MongoDB on one of machines to work as dataserver. You can read how to do so [[Configuring MongoDB as dataserver provider|here]].

#### Running engine

Engine is realized as web application on Java EE platform and thus needs to be deployed to application server, which in this case is glassfish. khrun script makes it easier to deploy engine to local (i.e. on the same machine) glassfish providing 4 mostly used commands:
* deploy - deploys application to glassfish (creates domain, starts it, adds necessary resources and deploys application)
* start - starts glassfish domain (if application was previously deployed you don't have to redeploy engine and only start domain)
* stop - stops glassfish domain
* clean - stops and deletes glassfish domain

To run script you need to first modify it's 3rd line and provide path to bin directory of glassfish installation e.g. when glassfish is installed in directory <pre>~/glassfish3</pre> set <pre>GLASSFISH_BIN_PATH=~/glassfish3/bin</pre>

Now to deploy application just run:
<pre>
./khrun engine deploy
</pre>

To stop domain run:
<pre>
./khrun engine stop
</pre>

To start domain again run:
<pre>
./khrun engine start
</pre>

And finally to remove engine permanently from glassfish run (it will stop and delete domain created on application deploy):
<pre>
./khrun engine clean
</pre>

If you don't specify 2nd argument (i.e. deploy/start/stop/clean) it will default to deploy, so you may deploy engine by typing just:
<pre>
./khrun engine
</pre>

When creating domain you will get asked for username and password for domain administrator. You may use defaults (user: admin pass:<empty>) by pressing <enter> or specify your own. If you specify your own you will have to type it each time you run script.

#### Running cluster


Simply run:
<pre>
./khrun cluster <cluster_hostname> <engine_hostname> <engine_port>
</pre>

Parameters <cluster_hostname> <engine_hostname> <engine_port> may be omitted and will default to localhost localhost 8080.

#### Running unit

Simply run:
<pre>
./khrun unit <cluster_hostname>
</pre>

#### Create and run HelloWorld program

In the previous step you run engine, cluster and unit thus creating infrastructure for executing OpenCL kernels in KernelHive. In this step you will create and run simple KernelHive project. All of this you will do in client application called gui.

##### Running GUI


Simply run:
<pre>
./khrun gui
</pre>

##### Checking infrastructure

From gui you may check if everything run correctly by checking infrastructure.

First set address to engine (if you are running engine on the same machine it will already be set to localhost by default, so you may skip this step). Go to Edit -> Preferences and set Engine base address.

Go to Tools -> Infrastructure browser and you will see currently working infrastructure starting from Engine and ending on Devices (CPUs and GPUs). If you now change something in infrastructure (e.g. run new unit) you may click Refresh in the bottom part of the window.

##### Create and run project

###### Create project

Go to File -> New, then set Project Name and Project Folder as you like and click OK. You should now see your project structure to the left and empty Workflow Editor to the right. Let's now add some workers.

###### Add data processor

In the left side of gui window choose Repository tab and drag and drop Processor Graph Node (DataProcessor) to Workflow Editor space. In pop-up window choose directory where to put created node - you may safely click Select and put it by default in Project Folder.

Now right click created node and choose Properties. You will see properties window for DataProcessor worker node.

In Source Files choose created source file and click Edit. In main window you will now see OpenCL kernel code for this node. This code calculates fields of rectangles which approximate the integral value of function f(x) = x. On input it gets consecutive x values which should be separated with identical intervals and on output for each x value it produces appropriate field of rectangle. For the purpose of this tutorial leave this code untouched.

Back in properties window for DataProcessor node, again choose source file, but this time click Details. You will see the execution properties that will be set when kernel is executed. For the purpose of this tutorial set outputSize to 4096. You may also change the first value of globalSizes to any divisor of 1024 e.g. 512 1 1 or 256 1 1. Similarly you may change first value of localSizes to any divisor of the first value of globalSizes e.g. if globalSizes is 512 1 1 you may set localSizes to 256 1 1 or 128 1 1 etc. When you change values appropriately press enter and then Save. We advise to go once again into Details to check if values were properly saved. Now press save in DataProcessor properties.

The project is now almost ready to run. It only needs some input data.

###### Add input data to MongoDB

First download input file from attachment:12345. This file contains consecutive values 0.0 1.0 2.0 ... 1023.0 saved as floats so they may be used as input for the kernel.
To add this file to mongo run following from the folder you saved the above file:
<pre>
mongofiles --db hive-dataserver -u hive-dataserver -p hive-dataserver --authenticationDatabase admin put 12345
</pre>
If you run MongoDB on some other machine just add --host <hostname>:<port>.
The above line will add the above file to mongo as file with the same name. If you wish you may also use another name for the file but it must be an integer number and it must be different than 1 or 1000000.

###### Run the project

Now your project is ready to run. In gui click Start Execution then Next. Set input data URL to <mongo_hostname> <mongo_port> <input_filename>. For default configuration when mongo is running on localhost it will be: localhost 27017 12345. Now click Next and finally Finish.

The project will now run. In unit output you may read information about execution and in gui choose Tools -> Workflow Executions to see the status of the job. In opened tab (Workflow Viewer) kernel execution will be finished when Status is completed. Now you may read the results.

###### Read results from MongoDB

Results are saved in MongoDB in files with names equals to consecutive numbers starting from 1000000 for each of output streams. Since DataProcessor produces only one output it will be saved in file with name 1000000. To download this file into current directory run:
<pre>
mongofiles --db hive-dataserver -u hive-dataserver -p hive-dataserver --authenticationDatabase admin get 1000000
</pre>

In the source data the consecutive values were separated by 1, so the areas of intervals are equals to x values and thus the result is the same as input. It was done on purpose to easily check whether kernel execution was done properly. To check it just run:
<pre>
diff 12345 1000000
</pre>
and the output should be empty.

**Congratulations you just executed your first KernelHive project!**
