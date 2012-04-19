/*
 * The singleton pattern template.
 * Copyright: Pawel Rosciszewski (roy@vlo.gda.pl)
 * Date: 08.06.2006
 */
#ifndef SINGLETON_H_
#define SINGLETON_H_

namespace KernelHive {

template <typename T>
class Singleton
{
    static T* instance;

protected:

    Singleton()
    {
        instance = static_cast<T*>(this);
    }

    ~Singleton()
    {
        instance = 0;
    }

public:


    static inline T* Get()
    {
    	if(!instance)
    		new T();
        return instance;
    }
};

template <typename T> T* Singleton <T>::instance = 0;

}

#endif /* SINGLETON_H */
