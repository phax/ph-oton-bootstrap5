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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.Map;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.style.ReturnsMutableCopy;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.cache.impl.ProviderCache;
import com.helger.collection.commons.CommonsArrayList;
import com.helger.collection.commons.CommonsHashMap;
import com.helger.collection.commons.ICommonsList;
import com.helger.collection.commons.ICommonsMap;
import com.helger.datetime.format.PDTFromString;
import com.helger.photon.uicore.datetime.IDateFormatBuilder;
import com.helger.text.compare.ComparatorHelper;

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

  private static final class Searcher
  {
    private String m_sRest;
    private final ICommonsMap <String, ETempusDominusFormatToken> m_aAllMatching = new CommonsHashMap <> ();
    private final Comparator <String> m_aComp = ComparatorHelper.getComparatorStringLongestFirst ();

    public Searcher (@NonNull final String sRest)
    {
      ValueEnforcer.notNull (sRest, "Rest");
      m_sRest = sRest;
    }

    public boolean hasMore ()
    {
      return m_sRest.length () > 0;
    }

    @Nullable
    public ETempusDominusFormatToken getNextToken ()
    {
      m_aAllMatching.clear ();
      for (final ETempusDominusFormatToken eToken : ETempusDominusFormatToken.values ())
      {
        final String sJavaToken = eToken.getJavaToken ();
        if (m_sRest.startsWith (sJavaToken))
          m_aAllMatching.put (sJavaToken, eToken);
      }
      if (m_aAllMatching.isEmpty ())
        return null;

      Map.Entry <String, ETempusDominusFormatToken> aEntry;
      if (m_aAllMatching.size () == 1)
        aEntry = m_aAllMatching.getFirstEntry ();
      else
        aEntry = m_aAllMatching.getSortedByKey (m_aComp).getFirstEntry ();
      m_sRest = m_sRest.substring (aEntry.getKey ().length ());
      return aEntry.getValue ();
    }

    public char getNextChar ()
    {
      final char ret = m_sRest.charAt (0);
      m_sRest = m_sRest.substring (1);
      return ret;
    }
  }

  private static final ProviderCache <String, Bootstrap5DateTimePickerFormatBuilder> CACHE;
  static
  {
    CACHE = ProviderCache.<String, Bootstrap5DateTimePickerFormatBuilder> builder ()
                         .name ("Bootstrap5DateTimePickerFormatBuilder.PatternCache")
                         .valueProvider (sJavaPattern -> {
                           ValueEnforcer.notNull (sJavaPattern, "JavaPattern");

                           // Do parsing
                           final Bootstrap5DateTimePickerFormatBuilder aDFB = new Bootstrap5DateTimePickerFormatBuilder ();
                           final Searcher aSearcher = new Searcher (sJavaPattern);
                           while (aSearcher.hasMore ())
                           {
                             final ETempusDominusFormatToken eToken = aSearcher.getNextToken ();
                             if (eToken != null)
                               aDFB.append (eToken);
                             else
                             {
                               // It's not a token -> use a single char and check for the next
                               // token
                               aDFB.append (aSearcher.getNextChar ());
                             }
                           }
                           return aDFB;
                         })
                         .build ();
  }

  @NonNull
  public static IDateFormatBuilder fromJavaPattern (@NonNull final String sJavaPattern)
  {
    ValueEnforcer.notEmpty (sJavaPattern, "JavaPattern");

    return CACHE.getFromCache (sJavaPattern);
  }
}
