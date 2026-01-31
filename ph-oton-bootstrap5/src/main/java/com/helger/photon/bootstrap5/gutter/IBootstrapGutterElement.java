/*
 * Copyright (C) 2025-2026 Philip Helger (www.helger.com)
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
package com.helger.photon.bootstrap5.gutter;

import org.jspecify.annotations.NonNull;

import com.helger.annotation.Nonnegative;
import com.helger.html.css.ICSSClassProvider;
import com.helger.photon.bootstrap5.grid.EBootstrapBreakpoint;

public interface IBootstrapGutterElement extends ICSSClassProvider
{
  int PARTS_NONE = -1;

  @NonNull
  EBootstrapBreakpoint getGridType ();

  @Nonnegative
  int getParts ();

  @NonNull
  ICSSClassProvider getCSSClassX ();

  @NonNull
  ICSSClassProvider getCSSClassY ();
}
