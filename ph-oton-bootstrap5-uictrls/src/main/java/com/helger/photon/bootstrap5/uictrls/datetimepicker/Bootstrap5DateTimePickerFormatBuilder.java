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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.style.ReturnsMutableCopy;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.cache.impl.Cache;
import com.helger.collection.commons.CommonsArrayList;
import com.helger.collection.commons.ICommonsList;
import com.helger.datetime.format.PDTFromString;
import com.helger.photon.uicore.datetime.IDateFormatBuilder;

public class Bootstrap5DateTimePickerFormatBuilder implements IDateFormatBuilder
{
  // List of Character or ETempusDominusFormatToken
  private final ICommonsList <Object> m_aList = new CommonsArrayList <> ();

  public Bootstrap5DateTimePickerFormatBuilder ()
  {}

  @NonNull
  public Bootstrap5DateTimePickerFormatBuilder append (@NonNull final ETempusDominusFormatToken eToken)
  {
    ValueEnforcer.notNull (eToken, "Token");
    m_aList.add (eToken);
    return this;
  }

  @NonNull
  public Bootstrap5DateTimePickerFormatBuilder append (final char c)
  {
    m_aList.add (Character.valueOf (c));
    return this;
  }

  @NonNull
  @ReturnsMutableCopy
  public ICommonsList <Object> getAllInternalObjects ()
  {
    return m_aList.getClone ();
  }

  @NonNull
  public String getJSCalendarFormatString ()
  {
    final StringBuilder aSB = new StringBuilder ();
    for (final Object o : m_aList)
      if (o instanceof final ETempusDominusFormatToken eToken)
        aSB.append (eToken.getJSToken ());
      else
        aSB.append (((Character) o).charValue ());
    return aSB.toString ();
  }

  @NonNull
  public String getJavaFormatString ()
  {
    final StringBuilder aSB = new StringBuilder ();
    for (final Object o : m_aList)
      if (o instanceof final ETempusDominusFormatToken eToken)
        aSB.append (eToken.getJavaToken ());
      else
        aSB.append (((Character) o).charValue ());
    return aSB.toString ();
  }

  @NonNull
  public LocalDate getDateFormatted (@Nullable final String sDate)
  {
    return PDTFromString.getLocalDateFromString (sDate, getJavaFormatString ());
  }

  @NonNull
  public LocalTime getTimeFormatted (@Nullable final String sTime)
  {
    return PDTFromString.getLocalTimeFromString (sTime, getJavaFormatString ());
  }

  @NonNull
  public LocalDateTime getLocalDateTimeFormatted (@Nullable final String sDateTime)
  {
    return PDTFromString.getLocalDateTimeFromString (sDateTime, getJavaFormatString ());
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("List", m_aList).getToString ();
  }

  // Cache for standard patterns
  private static final class PatternCache extends Cache <String, Bootstrap5DateTimePickerFormatBuilder>
  {
    public PatternCache ()
    {
      super (Bootstrap5DateTimePickerFormatBuilder::fromJavaPattern, "BS5DTPickerFormatCache");
    }
  }

  private static final PatternCache CACHE = new PatternCache ();

  @NonNull
  public static Bootstrap5DateTimePickerFormatBuilder fromJavaPattern (@NonNull final String sJavaPattern)
  {
    final Bootstrap5DateTimePickerFormatBuilder aBuilder = new Bootstrap5DateTimePickerFormatBuilder ();
    final int nLen = sJavaPattern.length ();
    for (int i = 0; i < nLen; ++i)
    {
      final char c = sJavaPattern.charAt (i);
      // TODO: This parsing logic needs to be robust enough to handle Java patterns
      // and map them to ETempusDominusFormatToken.
      // For now, we assume a simple mapping or direct character append if no token matches.
      // This is a simplified implementation and might need improvement.

      // Check for multi-char tokens first
      boolean bFound = false;

      // Try to match longest tokens first
      // This requires a reverse lookup map or iterating through tokens
      // For simplicity in this migration step, we might need a more robust parser later.

      // Fallback: just append char if not a known token start
      // Ideally we should use a proper parser here.

      // Let's try to match simple tokens
      if (c == 'y')
      {
        if (i + 3 < nLen && sJavaPattern.startsWith ("yyyy", i))
        {
          aBuilder.append (ETempusDominusFormatToken.YEAR_4_DIGITS);
          i += 3;
          bFound = true;
        }
        else
          if (i + 1 < nLen && sJavaPattern.startsWith ("yy", i))
          {
            aBuilder.append (ETempusDominusFormatToken.YEAR_2_DIGITS);
            i += 1;
            bFound = true;
          }
          else
          {
            aBuilder.append (ETempusDominusFormatToken.YEAR);
            bFound = true;
          }
      }
      else
        if (c == 'M')
        {
          if (i + 3 < nLen && sJavaPattern.startsWith ("MMMM", i))
          {
            aBuilder.append (ETempusDominusFormatToken.MONTH_FULL);
            i += 3;
            bFound = true;
          }
          else
            if (i + 2 < nLen && sJavaPattern.startsWith ("MMM", i))
            {
              aBuilder.append (ETempusDominusFormatToken.MONTH_ABBR);
              i += 2;
              bFound = true;
            }
            else
              if (i + 1 < nLen && sJavaPattern.startsWith ("MM", i))
              {
                aBuilder.append (ETempusDominusFormatToken.MONTH_2_DIGITS);
                i += 1;
                bFound = true;
              }
              else
              {
                aBuilder.append (ETempusDominusFormatToken.MONTH_NUMBER);
                bFound = true;
              }
        }
        else
          if (c == 'd')
          {
            if (i + 1 < nLen && sJavaPattern.startsWith ("dd", i))
            {
              aBuilder.append (ETempusDominusFormatToken.DAY_OF_MONTH_2_DIGITS);
              i += 1;
              bFound = true;
            }
            else
            {
              aBuilder.append (ETempusDominusFormatToken.DAY_OF_MONTH);
              bFound = true;
            }
          }
          else
            if (c == 'H')
            {
              if (i + 1 < nLen && sJavaPattern.startsWith ("HH", i))
              {
                aBuilder.append (ETempusDominusFormatToken.HOUR_0_23_2_DIGITS);
                i += 1;
                bFound = true;
              }
              else
              {
                aBuilder.append (ETempusDominusFormatToken.HOUR_0_23);
                bFound = true;
              }
            }
            else
              if (c == 'h')
              {
                if (i + 1 < nLen && sJavaPattern.startsWith ("hh", i))
                {
                  aBuilder.append (ETempusDominusFormatToken.HOUR_1_12_2_DIGITS);
                  i += 1;
                  bFound = true;
                }
                else
                {
                  aBuilder.append (ETempusDominusFormatToken.HOUR_1_12);
                  bFound = true;
                }
              }
              else
                if (c == 'm')
                {
                  if (i + 1 < nLen && sJavaPattern.startsWith ("mm", i))
                  {
                    aBuilder.append (ETempusDominusFormatToken.MINUTE_2_DIGITS);
                    i += 1;
                    bFound = true;
                  }
                  else
                  {
                    aBuilder.append (ETempusDominusFormatToken.MINUTE);
                    bFound = true;
                  }
                }
                else
                  if (c == 's')
                  {
                    if (i + 1 < nLen && sJavaPattern.startsWith ("ss", i))
                    {
                      aBuilder.append (ETempusDominusFormatToken.SECOND_2_DIGITS);
                      i += 1;
                      bFound = true;
                    }
                    else
                    {
                      aBuilder.append (ETempusDominusFormatToken.SECOND);
                      bFound = true;
                    }
                  }
                  else
                    if (c == 'a')
                    {
                      aBuilder.append (ETempusDominusFormatToken.AMPM);
                      bFound = true;
                    }

      if (!bFound)
        aBuilder.append (c);
    }
    return aBuilder;
  }
}
