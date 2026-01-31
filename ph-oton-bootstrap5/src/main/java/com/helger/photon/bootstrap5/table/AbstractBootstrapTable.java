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
package com.helger.photon.bootstrap5.table;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.html.hc.html.tabular.AbstractHCTable;
import com.helger.html.hc.html.tabular.IHCCol;
import com.helger.photon.bootstrap5.CBootstrapCSS;

/**
 * Abstract table with basic Bootstrap styling.
 *
 * @author Philip Helger
 * @param <IMPLTYPE>
 *        Implementation type
 */
public abstract class AbstractBootstrapTable <IMPLTYPE extends AbstractHCTable <IMPLTYPE>> extends AbstractHCTable <IMPLTYPE>
{
  private void _init ()
  {
    addClass (CBootstrapCSS.TABLE);
    setCondensed (true);
  }

  public AbstractBootstrapTable ()
  {
    super ();
    _init ();
  }

  public AbstractBootstrapTable (@Nullable final IHCCol <?> aCol)
  {
    super (aCol);
    _init ();
  }

  public AbstractBootstrapTable (@Nullable final IHCCol <?>... aCols)
  {
    super (aCols);
    _init ();
  }

  public AbstractBootstrapTable (@Nullable final Iterable <? extends IHCCol <?>> aCols)
  {
    super (aCols);
    _init ();
  }

  @NonNull
  public IMPLTYPE setStriped (final boolean bStriped)
  {
    if (bStriped)
      addClass (CBootstrapCSS.TABLE_STRIPED);
    else
      removeClass (CBootstrapCSS.TABLE_STRIPED);
    return thisAsT ();
  }

  @NonNull
  public IMPLTYPE setBordered (final boolean bBordered)
  {
    if (bBordered)
      addClass (CBootstrapCSS.TABLE_BORDERED);
    else
      removeClass (CBootstrapCSS.TABLE_BORDERED);
    return thisAsT ();
  }

  @NonNull
  public IMPLTYPE setBorderless (final boolean bBorderless)
  {
    if (bBorderless)
      addClass (CBootstrapCSS.TABLE_BORDERLESS);
    else
      removeClass (CBootstrapCSS.TABLE_BORDERLESS);
    return thisAsT ();
  }

  @NonNull
  public IMPLTYPE setHover (final boolean bHover)
  {
    if (bHover)
      addClass (CBootstrapCSS.TABLE_HOVER);
    else
      removeClass (CBootstrapCSS.TABLE_HOVER);
    return thisAsT ();
  }

  @NonNull
  public IMPLTYPE setCondensed (final boolean bCondensed)
  {
    if (bCondensed)
      addClass (CBootstrapCSS.TABLE_SM);
    else
      removeClass (CBootstrapCSS.TABLE_SM);
    return thisAsT ();
  }

  @NonNull
  public IMPLTYPE setDark (final boolean bDark)
  {
    if (bDark)
      addClass (CBootstrapCSS.TABLE_DARK);
    else
      removeClass (CBootstrapCSS.TABLE_DARK);
    return thisAsT ();
  }

  @NonNull
  public IMPLTYPE setResponsive (final boolean bResponsive)
  {
    if (bResponsive)
      addClass (CBootstrapCSS.TABLE_RESPONSIVE);
    else
      removeClass (CBootstrapCSS.TABLE_RESPONSIVE);
    return thisAsT ();
  }
}
