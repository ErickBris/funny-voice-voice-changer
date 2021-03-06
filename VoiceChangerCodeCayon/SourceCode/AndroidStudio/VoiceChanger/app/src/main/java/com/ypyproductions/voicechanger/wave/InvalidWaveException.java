/* InvalidWaveException.java

   Copyright (c) 2011 Ethan Chen

   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Lesser General Public
   License as published by the Free Software Foundation; either
   version 2 of the License, or (at your option) any later version.

   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public
   License along with this library; if not, write to the Free Software
   Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package com.ypyproductions.voicechanger.wave;

import java.io.IOException;

public class InvalidWaveException extends IOException {

    /**
     * Generated serialVersionUID
     */
    private static final long serialVersionUID = -8229742633848759378L;
    
    public InvalidWaveException() {
        
    }

    public InvalidWaveException(String msg) {
        super(msg);
    }
}
