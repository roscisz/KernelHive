# hive-gui

A KernelHive application is defined as an directed acyclic graph (DAG). Its nodes represent computational tasks and the edges point the data flow. A dynamic template repository provides various types of tasks. One or more source codes defined as OpenCL kernels are attached to every graph node, depending on the task type. Application defined in such way, along with attached source code and configuration compose a KernelHive project. Such projects can be edited using graphical tool **hive-gui** (Java Swing application) and stored in an XML file and a proper directory structure.
Running an application requires defining the URL of the input data. Data flow between individual tasks is implemented using distributed databases.

## Opening existing projects

* File -> Open...
* go to the project directory
* choose project.xml

## Creating new projects
* File -> New
* enter "project name"
* choose project directory

## Opening the graph editing window
* Tools -> Workflow Editor

## Opening the workflow executions window 
* Tools -> Workflow Executions
* guzik "Refresh" odświeża listę

## Graph editing
### Adding new node
* left panel, Repository tab
* choose the appropriate node, drag-and-drop it to the graph panel
* choose a directory for the kernels connected with the new node
	
### Editing graph node properties
* righ click on the node -> Properties
* view: node ID, node type
* edit: node name and properties
* choose a kernel source file and:
	* open it with double clicking
	* view its properties - select the file -> Details

## Submit the graph for execution

* Toolbar -> "Play" button ( > )
* first window is the graph validation window. If everything is OK, go further
* second window - insert the input data URL for the graph, Validate
* third window - login and password - OBSOLETE
* button Finish - send a serialized graph to the execution engine
* view the task in Workflow Executions