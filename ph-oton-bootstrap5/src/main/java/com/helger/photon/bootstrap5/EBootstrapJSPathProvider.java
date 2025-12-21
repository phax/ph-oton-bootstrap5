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
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonempty;
import com.helger.html.resource.js.ConstantJSPathProvider;
import com.helger.html.resource.js.IJSPathProvider;

/**
 * Contains default JS paths within this library.
 *
 * @author Philip Helger
 */
public enum EBootstrapJSPathProvider implements IJSPathProvider
{
  /** Default complete Bootstrap JS (requires Popper.js separately) */
  BOOTSTRAP ("external/bootstrap/5.3.8/bootstrap.js"),
  /** Bootstrap JS with bundled Popper.js - recommended for Bootstrap 5 */
  BOOTSTRAP_BUNDLE ("external/bootstrap/5.3.8/bootstrap.bundle.js"),
  /** Some Bootstrap JS extensions */
  BOOTSTRAP_PH ("ph-oton/bootstrap5-ph.js");

  private final ConstantJSPathProvider m_aPP;

  EBootstrapJSPathProvider (@NonNull @Nonempty final String sPath)
  {
    m_aPP = ConstantJSPathProvider.builder ().path (sPath).minifiedPathFromPath ().build ();
  }

  @NonNull
  @Nonempty
  public String getJSItemPath (final boolean bRegular)
  {
    return m_aPP.getJSItemPath (bRegular);
  }

  @Nullable
  public String getConditionalComment ()
  {
    return m_aPP.getConditionalComment ();
  }

  public boolean isBundlable ()
  {
    return m_aPP.isBundlable ();
  }
}
