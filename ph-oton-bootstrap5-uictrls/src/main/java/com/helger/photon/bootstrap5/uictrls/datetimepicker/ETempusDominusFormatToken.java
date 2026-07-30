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
 * Defines the possible tokens for the Tempus Dominus v6 format.<br>
 * Tempus Dominus v6 uses its own format tokens (see the <code>formattingTokens</code> regular
 * expression in tempus-dominus.js):
 * <code>T, t, yyyy, yy, y, MMMM, MMM, MM, M, dddd, ddd, dd, d, hh, h, HH, H, mm, m, ss, s</code>.
 * The meridiem token is <code>T</code>/<code>t</code> (not <code>a</code> as in Java) and the day
 * of week tokens are <code>ddd</code>/<code>dddd</code> (not <code>E</code> as in Java).
 *
 * @author Philip Helger
 */
public enum ETempusDominusFormatToken
{
  // Year
  YEAR ("y", "y"),
  YEAR_PROLEPTIC ("y", "u"),
  YEAR_2_DIGITS ("yy", "yy"),
  YEAR_4_DIGITS ("yyyy", "yyyy"),

  // Month
  MONTH_NUMBER ("M", "M"),
  MONTH_2_DIGITS ("MM", "MM"),
  MONTH_ABBR ("MMM", "MMM"),
  MONTH_FULL ("MMMM", "MMMM"),

  // Day of month
  DAY_OF_MONTH ("d", "d"),
  DAY_OF_MONTH_2_DIGITS ("dd", "dd"),

  // Day of week - Java "E", "EE" and "EEE" are all abbreviated
  DAY_OF_WEEK_ABBR ("ddd", "E"),
  DAY_OF_WEEK_ABBR_2 ("ddd", "EE"),
  DAY_OF_WEEK_ABBR_3 ("ddd", "EEE"),
  DAY_OF_WEEK_FULL ("dddd", "EEEE"),

  // AM/PM
  AMPM ("T", "a"),

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
