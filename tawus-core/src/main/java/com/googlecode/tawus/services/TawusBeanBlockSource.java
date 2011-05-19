package com.googlecode.tawus.services;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.services.BeanBlockSource;

public interface TawusBeanBlockSource {
   Block getDisplayBlock(String dataType);
   Block getEditBlock(String editBlock);
   boolean hasDisplayBlock(String dataType);
   BeanBlockSource getBeanBlockSource();
}
