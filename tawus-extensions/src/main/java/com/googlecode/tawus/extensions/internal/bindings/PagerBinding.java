/*
 * Copyright 2010 Taha Hafeez
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.tawus.extensions.internal.bindings;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.internal.bindings.ComponentBinding;
import org.apache.tapestry5.ioc.Location;

import com.googlecode.tawus.extensions.internal.Pageable;

public class PagerBinding extends ComponentBinding{

   public PagerBinding(Location location, String description,
      ComponentResources resources, String componentId) {
      super(location, description, resources, componentId);
   }
   
   public Object get(){
      return ((Pageable)super.get()).getPagedSource();
   }
   
   @SuppressWarnings({ "rawtypes" })
   public Class getBindingType(){
      return Iterable.class;
   }

}
