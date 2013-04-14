#ifndef KERNEL_HIVE_BASIC_WORKER_H
#define KERNEL_HIVE_BASIC_WORKER_H

#include <map>
#include <vector>

#include "commons/Worker.h"
#include "commons/OpenClDevice.h"
#include "commons/ExecutionContext.h"
#include "threading/SynchronizedBuffer.h"
#include "../communication/DataDownloader.h"
#include "../communication/DataUploader.h"

namespace KernelHive {

typedef std::map<int, DataDownloader *> DownloaderMap;
typedef std::vector<DataUploader *> UploaderList;
typedef std::map<int, SynchronizedBuffer *> DataBufferMap;

/**
 * A a base class for basic worker types.
 *
 * Parameters currently expected as input:
 * <ul>
 *   <li>data provider host</li>
 *   <li>data provider port</li>
 *   <li>kernel provider host</li>
 *   <li>kernel provider port</li>
 *   <li>device identifier</li>
 *   <li>number of dimensions to use - n</li>
 *   <li>n numbers specifying the offset in each dimension</li>
 *   <li>n numbers specifying the global workgroup size in each dimension</li>
 *   <li>n numbers specifying the local workgroup size in each dimension</li>
 *   <li>kernel code identifier</li>
 * </ul>
 */
class BasicWorker : public Worker {

public:
	/**
	 * Initializes this BasicWorker instance.
	 *
	 * @param clusterAddress the address of the KernelHive cluster.
	 */
	BasicWorker(char **argv);

	/**
	 * The destructor.
	 */
	virtual ~BasicWorker();

	/**
	 * The processing logic should be implemented in this method.
	 *
	 * @param argv the parameters passed to the worker by the KernelHive
	 * 		system
	 */
	void work(char *const argv[]);

	/**
	 * Returns a pointer to the vector of DataUploaders
	 * used by this worker.
	 *
	 * @return the pointer to vector of uploaders used by this
	 * 		worker
	 */
	UploaderList* getUploaders();

protected:
	/** The execution context name of the input data buffer. */
	static const char* INPUT_BUFFER;

	/** The execution context name of the output data buffer. */
	static const char* OUTPUT_BUFFER;

	/** The address from which the kernel can be downloaded. */
	NetworkAddress* kernelAddress;

	/** Map for storing data downloaders of this worker. */
	DownloaderMap downloaders;

	/** A map for storing data uploaders of this worker. */
	UploaderList uploaders;

	/** A map for storing buffers of this worker. */
	DataBufferMap buffers;

	/** The kernel identifier. */
	std::string deviceId;

	/** The number of dimensions to use for kernel execution. */
	unsigned int numberOfDimensions;

	/** An array of offsets in each dimensions. */
	size_t* dimensionOffsets;

	/** Global workgroup sizes in each dimension. */
	size_t* globalSizes;

	/** Local workgroup sizes in each dimension. */
	size_t* localSizes;

	/** Defines the size of the output that will be produced by this worker. */
	size_t outputSize;

	/** The OpenCL device which will be used for execution. */
	OpenClDevice* device;

	/** The execution context in which the data will be processed. */
	ExecutionContext* context;

	/** The identifier which can be used to download the kernel. */
	std::string kernelDataId;

	/** Kernel data identifier as an integer number. */
	int kernelDataIdInt;

	/**
	 * Performs initialization specific to a worker subclassing this.
	 *
	 * @param argv the parameters specific for a given BasicWorker subclass
	 */
	virtual void initSpecific(char *const argv[]) = 0;

	/**
	 * The method in which a concrete BasicWorker implementation should perform
	 * it's processing logic.
	 */
	virtual void workSpecific() = 0;

	/**
	 * Returns the name of the kernel to use for calculations.
	 *
	 * @return the name of the kernel to use.
	 */
	virtual const char* getKernelName() = 0;

	/**
	 * Runs or downloader threads stored in the map.
	 */
	void runAllDownloads();

	/**
	 * Waits until all downloads finish.
	 */
	void waitForAllDownloads();

	/**
	 * Runs all uploader threads stored in the map.
	 */
	void runAllUploads();

	/**
	 * Runs all uploaders threads stored in the map synchronously.
	 */
	void runAllUploadsSync();

	/**
	 * Waits until all uploads finish.
	 */
	void waitForAllUploads();

	/**
	 * Returns concatenated list of uploaded data ID's
	 */
	const void getAllUploadIDStrings(std::string* param);


private:
	/**
	 * A generic method initializing a BasicWorker.
	 *
	 * @param argv the parameters passed to the worker
	 */
	void init(char *const argv[]);

	/**
	 * Deallocates resources used by this worker.
	 */
	void deallocateResources();

};

} /* namespace KernelHive */

#endif /* KERNEL_HIVE_BASIC_WORKER_H */
