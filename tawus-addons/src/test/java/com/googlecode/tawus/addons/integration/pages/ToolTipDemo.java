package com.googlecode.tawus.addons.integration.pages;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ioc.annotations.Inject;

public class ToolTipDemo
{
   @Inject
   private Block toolTip;
   
   Object onToolTip()
   {
      return toolTip;
   }
}
