###
# KernelHive template runners Makefile
#

# Compiler settings:
COMPILER = g++
COMPILER_FLAGS = -Wall
DEBUG_FLAGS =  -g -pthread
OPENCL_FLAGS = -l OpenCL -L ${LD_LIBRARY_PATH} -I ${CPLUS_INCLUDE_PATH}

# Search path:
VPATH = src:src/commons

# Build directories:
OUTPUT_DIR = out

# The executable file:
EXECUTABLE = commons-test

# Object files genereated during the build:
OBJECTS = \
	OpenClException.o \
	OpenClHelper.o \
	OpenClPlatform.o \
	OpenClDevice.o \
	OpenClHost.o \
	Main.o

# The default target:
default : all

# The 'clean' rule:
clean :
	rm -fR $(EXECUTABLE) $(OUTPUT_DIR) *.o

# The 'all' rule:
all : $(EXECUTABLE)
	mkdir -p $(OUTPUT_DIR)
	mv $(OBJECTS) $(OUTPUT_DIR)
	mv $^ $(OUTPUT_DIR)

# Build all the classes:
compile: $(OBJECTS)
	mkdir -p $(OUTPUT_DIR)
	mv $(OBJECTS) $(OUTPUT_DIR)

# Executable file building rule:
$(EXECUTABLE) : $(OBJECTS)
	$(COMPILER) $(COMPILER_FLAGS) $(DEBUG_FLAGS) $(OPENCL_FLAGS) -o $@ $^

# The pattern for object files:
%.o : %.cpp
	$(COMPILER) $(COMPILER_FLAGS) $(DEBUG_FLAGS) $(OPENCL_FLAGS) -c $^ -o $@

