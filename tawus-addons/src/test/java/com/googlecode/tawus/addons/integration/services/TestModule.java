// 
// Copyright 2011 Taha Hafeez Siddiqi (tawushafeez@gmail.com)
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//   http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// 
package com.googlecode.tawus.addons.integration.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.services.ComponentClassTransformWorker;

import com.googlecode.tawus.addons.internal.transform.InjectSelectSupportWorker;
import com.googlecode.tawus.addons.services.TawusAddonsModule;

@SubModule(TawusAddonsModule.class)
public class TestModule
{
   public static void contributeApplicationDefaults(final MappedConfiguration<String, Object> defaults)
   {
      defaults.add(SymbolConstants.PRODUCTION_MODE, "false");
   }
   
   public static void contributeComponentClassTransformWorker(OrderedConfiguration<ComponentClassTransformWorker> configuration){
      configuration.addInstance("InjectSelectSupportWorker", InjectSelectSupportWorker.class);
   }
}
