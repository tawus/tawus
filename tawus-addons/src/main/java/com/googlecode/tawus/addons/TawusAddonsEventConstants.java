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
package com.googlecode.tawus.addons;

/**
 * Events Constants
 */
public class TawusAddonsEventConstants
{
   /**
    * Server event triggered by {@link com.googlecode.tawus.addons.components.ModalDialog ModalDialog}
    * to fetch the block which is to be displayed in the dialog. Event handler should return the block to be 
    * displayed.
    */
   public static final String SHOW_DIALOG = "showDialog";
   
   /**
    * Event triggered by {@link com.googlecode.tawus.addons.components.ModalDialog ModalDialog}
    * after it has been closed/hidden. 
    */
   public static final String CLOSE_DIALOG = "closeDialog";
   
   
   public static final String REFRESH = "refresh";
}
