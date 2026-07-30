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
package com.helger.photon.bootstrap5.uictrls.datetimepicker;

import org.jspecify.annotations.NonNull;

import com.helger.annotation.Nonempty;

/**
 * Defines the special format shortcuts of DTP.<br>
 * Tempus Dominus v6 replaces these tokens with the formats defined in
 * <code>localization.dateFormats</code> (see the defaults in tempus-dominus.js). Only the uppercase
 * variants (LTS, LT, L, LL, LLL, LLLL) are supported - the lowercase Moment.js variants no longer
 * exist.
 *
 * @author Philip Helger
 */
public enum EBootstrap5DateTimePickerSpecialFormats
{
  TIME ("LT"),
  TIME_WITH_SECONDS ("LTS"),
  MONTHNUM_DAY_YEAR ("L"),
  MONTHNAME_DAY_YEAR ("LL"),
  MONTHNAME_DAY_YEAR_TIME ("LLL"),
  DAYOFWEEK_MONTHNAME_DAY_YEAR_TIME ("LLLL");

  private final String m_sFormatString;

  EBootstrap5DateTimePickerSpecialFormats (@NonNull @Nonempty final String sFormatString)
  {
    m_sFormatString = sFormatString;
  }

  @NonNull
  @Nonempty
  public String getFormatString ()
  {
    return m_sFormatString;
  }
}
