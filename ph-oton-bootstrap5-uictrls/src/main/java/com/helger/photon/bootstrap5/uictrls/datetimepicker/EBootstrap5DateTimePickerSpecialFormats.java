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
package com.helger.photon.bootstrap5.uictrls.datetimepicker;

import org.jspecify.annotations.NonNull;

import com.helger.annotation.Nonempty;

/**
 * Defines the different today types of DTP.<br>
 * Tempus Dominus v6 supports Intl.DateTimeFormat options, but also supports
 * some localized format strings if using custom formats.
 * However, since we are moving away from Moment.js, these specific tokens (L, LT, etc.)
 * are part of the Intl.DateTimeFormat standard options or custom implementations in TD6.
 * <p>
 * According to TD6 docs, it supports `L`, `LT`, `LTS` etc. as shortcuts for Intl configurations.
 * </p>
 *
 * @author Philip Helger
 */
public enum EBootstrap5DateTimePickerSpecialFormats
{
  TIME ("LT"),
  TIME_WITH_SECONDS ("LTS"),
  MONTHNUM_DAY_YEAR_FULL ("L"),
  MONTHNUM_DAY_YEAR ("l"),
  MONTHNAME_DAY_YEAR_FULL ("LL"),
  MONTHNAME_DAY_YEAR ("ll"),
  MONTHNAME_DAY_YEAR_TIME_FULL ("LLL"),
  MONTHNAME_DAY_YEAR_TIME ("lll"),
  DAYOFWEEK_MONTHNAME_DAY_YEAR_TIME_FULL ("LLLL"),
  DAYOFWEEK_MONTHNAME_DAY_YEAR_TIME ("llll");

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
