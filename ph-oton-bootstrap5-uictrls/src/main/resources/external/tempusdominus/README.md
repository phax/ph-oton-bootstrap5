# Tempus Dominus in ph-oton-bootstrap5

Source: https://github.com/Eonasdan/tempus-dominus — https://getdatepicker.com/
License: MIT

Currently shipped: **6.10.4** (`6.10.4/`), driven by
`com.helger.photon.bootstrap5.uictrls.datetimepicker.BootstrapDateTimePicker`.

## The shipped files are unmodified

The four files below are byte-identical to the official npm release. **Do not patch them** — every
ph-oton specific behaviour is implemented in the Java wrapper instead, so that a new Tempus Dominus
version can simply be dropped in.

| File | Origin |
|------|--------|
| `tempus-dominus.js` | `https://cdn.jsdelivr.net/npm/@eonasdan/tempus-dominus@<version>/dist/js/tempus-dominus.js` |
| `tempus-dominus.min.js` | `.../dist/js/tempus-dominus.min.js` |
| `tempus-dominus.css` | `.../dist/css/tempus-dominus.css` |
| `tempus-dominus.min.css` | `.../dist/css/tempus-dominus.min.css` |

The `.min` files come from the release as well — `ph-jscompress-maven-plugin` / `ph-csscompress-maven-plugin`
leave them alone because they already exist. The locales, plugins, ESM builds, jQuery provider and
`*.map` files of the npm package are deliberately **not** shipped: localization comes from
`EBootstrap5DateTimePickerTexts` and the picker is initialized as a plain JS object.

Verify a candidate download against the shipped copy with a plain `diff` before committing it.

## The deltas — what ph-oton adds on top of a clean Tempus Dominus

Everything in this list lives in Java and must be re-checked after an upgrade. The numbers are
referenced from the upgrade checklist below.

**D1 — Initialization without jQuery.** `BootstrapDateTimePicker.invoke ()` emits
`new tempusDominus.TempusDominus (document.getElementById ('<id>'), <options>)`. Tempus Dominus 6 has no
jQuery plugin API any more (Bootstrap 4 used `$(...).datetimepicker (...)`). The generated script is a
`Bootstrap5DateTimePickerJS` out-of-band node; `Bootstrap5DateTimePickerSpecialNodeListModifier` merges
the initialization of all pickers on a page that share identical options into a single invocation.

**D2 — FontAwesome 5 icons.** The Tempus Dominus defaults are FontAwesome **6** class names
(`fa-solid fa-calendar`), while ph-oton ships FontAwesome 5 (`fa fa-calendar`). The constructor therefore
fills `display.icons` explicitly for all nine keys: `time`, `date`, `up`, `down`, `previous`, `next`,
`today`, `clear`, `close`. If the icon set of the application changes, this is the single place to adapt.

**D3 — Input group markup.** The picker is a `BootstrapInputGroup`, not a bare input:

* the group carries `data-td-target-input="nearest"` and the CSS class `date`,
* the `<input>` carries `data-td-target="#<groupID>"` and the CSS class `datetimepicker-input`,
* with a prepend icon, a `<span class="input-group-text" data-td-target="#<groupID>" data-td-toggle="datetimepicker">`
  is inserted **at the front** of the input group (via the `protected` `BootstrapInputGroup.addChildPrefixAtFront`).

**D4 — Conditional toggle wiring.** `data-td-target-toggle="nearest"` is only set when a prepend icon
exists. Without an icon, Tempus Dominus uses the whole input group as the toggle, and up to 6.9.4 a
`data-td-target-toggle` that resolves to nothing made initialization throw. Since 6.10.0 the null case
returns early instead of throwing, but the conditional is still correct and should stay.

**D5 — `allowInputToggle` only with an icon.** Clicking or focusing the input opens the picker, as in the
Bootstrap 4 version. Without a prepend icon the option must stay off, because then the input group itself
is the toggle and the two handlers cancel each other out.

**D6 — Java date patterns.** `Bootstrap5DateTimePickerFormatBuilder` / `ETempusDominusFormatToken` translate
`java.time` patterns into Tempus Dominus tokens (`EEEE` → `dddd`, `a` → `T`, `uuuu` → `yyyy`, …). Tempus
Dominus 6 no longer uses moment.js, so the Bootstrap 4 mapping (`EMomentsDateTimePickerFormatToken`) does
not apply. `ETempusDominusFormatTokenTest` checks that every declared token still round-trips.

**D7 — Options derived from the effective format.** `localization.hourCycle` is set to `h23` for `H`
patterns and `h12` for `h` patterns, and `display.components.seconds` is enabled when the format contains
`s`. Tempus Dominus does not derive any of this by itself.

**D8 — German/English localization.** All 29 `localization` text keys are filled from
`EBootstrap5DateTimePickerTexts`, plus `locale` (BCP-47 tag) and `format`. Keys not listed there keep the
Tempus Dominus English default.

**D9 — Dates as JS `Date`.** `defaultDate` and `restrictions.minDate` / `maxDate` are emitted as
`new Date (y, m-1, d, h, mi, s)` — note the 0-based month.

**D10 — Popper 2 first.** `registerResourcesForThisRequest ()` registers Popper **before** Tempus Dominus,
because Tempus Dominus expects a global `window.Popper` when it opens the popup. Popper is not part of the
Tempus Dominus package and is vendored separately under `external/popperjs/`.

**D11 — Event name.** `BootstrapDateTimePicker.EVENT_NAME_CHANGE` is `change.td` (Tempus Dominus 6
namespace `.td`).

`BootstrapDateTimePickerTest` pins D1–D5, D8 and the option schema; run it first after any upgrade.

## Upgrade checklist

1. Download the four files listed above for the new version into `<version>/` and delete the old folder.
2. `EBootstrapUICtrlsJSPathProvider.DATETIMEPICKER` and `EBootstrapUICtrlsCSSPathProvider.DATETIMEPICKER` —
   adapt the paths. The path providers derive the `.min` name automatically, and
   `EBootstrapUICtrls*PathProviderTest` fails if a file is missing.
3. `ThirdPartyModuleProvider_ph_oton_bootstrap5_uictrls.TEMPUS_DOMINUS` — adapt the `Version`.
4. Diff `DefaultOptions` and `defaultEnLocalization` in the new `tempus-dominus.js` against the old one.
   **Every option and localization key the wrapper sends must still exist** — Tempus Dominus validates the
   option object and throws a `TdError` ("… is not a known option") for anything it does not know.
   New keys are optional; removed or renamed keys are breaking.
5. Re-check D2 (icon key names), D3/D4 (the `data-td-*` attribute names), D6 (format tokens) and D11
   (event namespace) against the new source.
6. `mvn -pl ph-oton-bootstrap5-uictrls -am test`, then open the `PagePublicDateTimePicker` page of
   `ph-oton-bootstrap5-demo` in a browser and check: the popup opens from the icon *and* from the input,
   the icons are visible, the texts are German, and a picked date arrives in the input.
7. Update the "News and Noteworthy" section in the root `README.md`.

## Version history of this integration

### 6.9.4 → 6.10.4 (upstream 2023-12-21 → 2025-05-07)

Upstream changes, none of them breaking for the wrapper:

* **Keyboard navigation and ARIA improvements** (6.10.0), made switchable in 6.10.1 through the new option
  `display.keyboardNavigation` (default `true`). Exposed as `BootstrapDateTimePicker.setKeyboardNavigation (...)`
  and only emitted when explicitly set.
* New localization key **`toggleAriaLabel`** (default `Change date`), used as the `aria-label` of the toggle
  element and extended with the picked date. Filled from `EBootstrap5DateTimePickerTexts.TOGGLE_ARIA_LABEL`.
* A `null` toggle element no longer throws (see D4).
* CSS: the widget `z-index` moved into the new `--td-widget-z-index` variable, and a focus ring
  (`.tempus-dominus-widget :focus`) was added for the keyboard navigation.
* Bug fixes #2950, #2951, removal of a stray `console.log` and of the `data-value` attributes on day cells.
* Cosmetic upstream quirk: the file banner reads `Tempus Dominus vv6.10.4` and the internal constant is
  `const version = 'v6.10.4'` (it was `'6.9.4'` before) — do not parse it.

### Known upstream quirks

* `localization.dayViewHeaderFormat` defaults to `{ month: 'long', year: '2-digit' }` in 6.9.4 and 6.10.4
  alike, so the calendar header reads "August 26" instead of "August 2026". The wrapper does not override
  the key - set it explicitly if a four digit year is wanted.

### Bootstrap 4 comparison

`ph-oton-bootstrap4` ships `tempusdominus-bootstrap-4` **5.39.0**, a different library: a jQuery plugin that
requires moment.js and uses a flat option schema (`showClear`, `tooltips`, `minDate`, …). Those vendored files
are unmodified upstream copies as well (they differ from the npm tarball only in line endings), so the same
rule applies there: customize in Java, not in the vendored file.

The public wrapper API was kept compatible on purpose — the Bootstrap 5 wrapper has the same setters as the
Bootstrap 4 one (`setFormat`, `setMode`, `setMinDate`, `setMaxDate`, `setShowToday`, `setShowClear`,
`setShowClose`, `setShowCalendarWeeks`, `setSideBySide`, `setUseCurrent`, `setViewMode`, `setPrependIcon`,
`setReadOnly`, `setInitialDate`), plus `setKeyboardNavigation` for the Tempus Dominus 6.10 feature. What
changed is everything below that surface: initialization (D1), the option schema (flat → nested
`display` / `localization` / `restrictions`), the format tokens (moment → Tempus Dominus, D6), and the icon
defaults (D2).
