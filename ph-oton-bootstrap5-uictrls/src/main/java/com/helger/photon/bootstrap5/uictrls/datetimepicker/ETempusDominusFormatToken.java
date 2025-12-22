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
 * Defines the possible tokens for the Tempus Dominus v6 format.
 * <p>
 * Note: Tempus Dominus v6 uses Intl.DateTimeFormat tokens (similar to Java) instead of Moment.js
 * tokens.
 * </p>
 *
 * @author Philip Helger
 */
public enum ETempusDominusFormatToken
{
  // Era
  ERA ("G", "G"),

  // Year
  YEAR ("y", "u"),
  YEAR_2_DIGITS ("yy", "yy"),
  YEAR_4_DIGITS ("yyyy", "yyyy"),

  // Month
  MONTH_NUMBER ("M", "M"),
  MONTH_2_DIGITS ("MM", "MM"),
  MONTH_ABBR ("MMM", "MMM"),
  MONTH_FULL ("MMMM", "MMMM"),

  // Day
  DAY_OF_MONTH ("d", "d"),
  DAY_OF_MONTH_2_DIGITS ("dd", "dd"),

  // Day of Week
  DAY_OF_WEEK_ABBR ("E", "E"),
  DAY_OF_WEEK_FULL ("EEEE", "EEEE"),

  // AM/PM
  // Java 'a' maps to 'a' or 't' in Intl? TD6 docs say 't' for short, 'tt' for long?
  // Actually TD6 supports 'T' for uppercase T/F?
  // Let's stick to standard Java/Intl mapping if possible.
  // TD6 uses `Intl.DateTimeFormat` options under the hood if no custom format is provided.
  // But for `customDateFormat`, it parses tokens.
  // According to TD6 docs:
  // t: a/p
  // T: A/P
  // h: 1-12
  // H: 0-23
  // m: 0-59
  // s: 0-59
  // y: year
  // M: month
  // d: day
  AMPM ("a", "a"),

  // Time
  HOUR_1_12 ("h", "h"),
  HOUR_1_12_2_DIGITS ("hh", "hh"),
  HOUR_0_23 ("H", "H"),
  HOUR_0_23_2_DIGITS ("HH", "HH"),
  MINUTE ("m", "m"),
  MINUTE_2_DIGITS ("mm", "mm"),
  SECOND ("s", "s"),
  SECOND_2_DIGITS ("ss", "ss");

  private final String m_sJSToken;
  private final String m_sJavaToken;

  ETempusDominusFormatToken (@NonNull @Nonempty final String sJSToken, @NonNull @Nonempty final String sJavaToken)
  {
    m_sJSToken = sJSToken;
    m_sJavaToken = sJavaToken;
  }

  @NonNull
  public String getJSToken ()
  {
    return m_sJSToken;
  }

  @NonNull
  public String getJavaToken ()
  {
    return m_sJavaToken;
  }
}
