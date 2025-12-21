/*
 * Copyright (C) 2018-2025 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.photon.bootstrap5;

import org.jspecify.annotations.NonNull;

import com.helger.annotation.Nonempty;
import com.helger.annotation.concurrent.ThreadSafe;
import com.helger.base.concurrent.SimpleReadWriteLock;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.collection.commons.CommonsArrayList;
import com.helger.collection.commons.ICommonsList;
import com.helger.html.resource.css.ICSSPathProvider;
import com.helger.html.resource.js.IJSPathProvider;

/**
 * Customize the global Bootstrap JS and CSS to be used. This is helpful when using a custom
 * Bootstrap build.
 * <p>
 * Note: Bootstrap 5 uses Popper.js v2, which is included in bootstrap.bundle.js.
 * If using bootstrap.js (without bundle), Popper.js must be included separately.
 * Unlike Bootstrap 4, jQuery is no longer required.
 *
 * @author Philip Helger
 */
@ThreadSafe
public final class BootstrapCustomConfig
{
  private static final SimpleReadWriteLock RW_LOCK = new SimpleReadWriteLock ();
  private static final ICommonsList <ICSSPathProvider> CSS = new CommonsArrayList <> ();
  private static final ICommonsList <IJSPathProvider> JS = new CommonsArrayList <> ();

  static
  {
    // Set default values: the default plain CSS and JS
    CSS.add (EBootstrapCSSPathProvider.BOOTSTRAP);
    CSS.add (EBootstrapCSSPathProvider.BOOTSTRAP_PH);

    // Bootstrap 5 uses bundle version by default (includes Popper.js v2)
    // No jQuery required!
    JS.add (EBootstrapJSPathProvider.BOOTSTRAP_BUNDLE);
    JS.add (EBootstrapJSPathProvider.BOOTSTRAP_PH);
  }

  private BootstrapCustomConfig ()
  {}

  public static void setBootstrapCSS (@NonNull @Nonempty final ICSSPathProvider... aCSSPathProvider)
  {
    ValueEnforcer.notEmptyNoNullValue (aCSSPathProvider, "CSSPathProvider");

    RW_LOCK.writeLocked ( () -> CSS.setAll (aCSSPathProvider));
  }

  @NonNull
  @Nonempty
  public static ICommonsList <ICSSPathProvider> getAllBootstrapCSS ()
  {
    return RW_LOCK.readLockedGet (CSS::getClone);
  }

  public static void setBootstrapJS (@NonNull @Nonempty final IJSPathProvider... aJSPathProvider)
  {
    ValueEnforcer.notEmptyNoNullValue (aJSPathProvider, "JSPathProvider");

    RW_LOCK.writeLocked ( () -> JS.setAll (aJSPathProvider));
  }

  @NonNull
  @Nonempty
  public static ICommonsList <IJSPathProvider> getAllBootstrapJS ()
  {
    return RW_LOCK.readLockedGet (JS::getClone);
  }
}
