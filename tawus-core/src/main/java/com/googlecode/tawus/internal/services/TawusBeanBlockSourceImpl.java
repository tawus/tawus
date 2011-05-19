package com.googlecode.tawus.internal.services;

import java.util.Collection;

import org.apache.tapestry5.internal.services.BeanBlockSourceImpl;
import org.apache.tapestry5.internal.services.RequestPageCache;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.BeanBlockSource;

import com.googlecode.tawus.services.TawusBeanBlockOverrideSource;
import com.googlecode.tawus.services.TawusBeanBlockSource;

public class TawusBeanBlockSourceImpl extends BeanBlockSourceImpl implements TawusBeanBlockSource {

   public TawusBeanBlockSourceImpl(RequestPageCache pageCache,
         TawusBeanBlockOverrideSource overrideSource, Collection<BeanBlockContribution> configuration) {
      super(pageCache, overrideSource.getBeanBlockOverrideSource(), configuration);
   }

   public BeanBlockSource getBeanBlockSource() {
      return (BeanBlockSource)this;
   }

}
