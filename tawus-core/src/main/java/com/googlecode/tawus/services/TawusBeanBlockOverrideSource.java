package com.googlecode.tawus.services;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.services.BeanBlockOverrideSource;

public interface TawusBeanBlockOverrideSource
{
   Block getDisplayBlock(String dataType);

   Block getEditBlock(String editBlock);

   boolean hasDisplayBlock(String dataType);

   BeanBlockOverrideSource getBeanBlockOverrideSource();
}
