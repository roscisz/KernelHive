/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
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
