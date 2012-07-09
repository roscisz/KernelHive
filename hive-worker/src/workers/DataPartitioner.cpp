#include "DataPartitioner.h"

namespace KernelHive {

// ========================================================================= //
// 							Constants Init									 //
// ========================================================================= //



// ========================================================================= //
// 							Public Members									 //
// ========================================================================= //

DataPartitioner::DataPartitioner(char **argv) : BasicWorker(argv) {
	// TODO Auto-generated constructor stub
}

DataPartitioner::~DataPartitioner() {
	// TODO Auto-generated destructor stub
}

// ========================================================================= //
// 							Protected Members							     //
// ========================================================================= //

const char* DataPartitioner::getKernelName() {
	return NULL;
}

void DataPartitioner::workSpecific() {

}

void DataPartitioner::initSpecific(char *const argv[]) {

}

// ========================================================================= //
// 							Private Members									 //
// ========================================================================= //

} /* namespace KernelHive */
