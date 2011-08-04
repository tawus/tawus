package com.googlecode.tawus.internal.services;

import java.util.Collection;

import org.apache.tapestry5.internal.services.BeanBlockOverrideSourceImpl;
import org.apache.tapestry5.internal.services.RequestPageCache;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.BeanBlockOverrideSource;

import com.googlecode.tawus.services.TawusBeanBlockOverrideSource;

public class TawusBeanBlockOverrideSourceImpl extends BeanBlockOverrideSourceImpl implements
      TawusBeanBlockOverrideSource
{

   public TawusBeanBlockOverrideSourceImpl(RequestPageCache pageCache, Collection<BeanBlockContribution> configuration)
   {
      super(pageCache, configuration);
   }

   public BeanBlockOverrideSource getBeanBlockOverrideSource()
   {
      return this;
   }

}
