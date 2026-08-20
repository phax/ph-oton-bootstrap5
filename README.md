# ph-oton-bootstrap5

Java wrapper library for Bootstrap 5 UI components, providing type-safe server-side rendering for web applications.

## Project Overview

This set of Java libraries forms a package to build Java web applications with Bootstrap 5.

### Contained Subprojects

* **ph-oton-bootstrap5** - Java Wrapper for the Bootstrap 5 controls
* **ph-oton-bootstrap5-uictrls** - Special UI controls for Bootstrap 5
* **ph-oton-bootstrap5-pages** - Predefined UI pages with Bootstrap 5 styling
* **ph-oton-bootstrap5-stub** - Servlet stub for Bootstrap 5 web applications
* **ph-oton-bootstrap5-demo** - A standalone demo web application to be run in Tomcat or in provided Jetty

## Requirements

* **Java 17+** is required for building 
* **Application server requirement JakartaEE 10:**
  * At least Tomcat 10.1
  * Jetty 12.x with AnnotationConfiguration enabled

## Maven Usage

Replace `x.y.z` with the effective version number.

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>com.helger.photon</groupId>
      <artifactId>ph-oton-bootstrap5-parent-pom</artifactId>
      <version>x.y.z</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

### Bootstrap 5

To use Bootstrap 5 front end:

```xml
<dependencies>
  <dependency>
    <groupId>com.helger.photon</groupId>
    <artifactId>ph-oton-bootstrap5-stub</artifactId>
  </dependency>
</dependencies>
```

See the submodule `ph-oton-bootstrap5-demo` for a working example project with Bootstrap 5 UI.

## Key Features

* Type-safe Java wrapper for Bootstrap 5 components
* Server-side component rendering
* Support for all major Bootstrap 5 components:
  * Grid system with XXL breakpoint
  * Forms with floating labels
  * Cards, modals, navbars, and dropdowns
  * New Offcanvas component
  * Alerts, badges, buttons, and more
* Integration with ph-oton framework
* JakartaEE 10 compatible

## Bootstrap 5 Highlights

This library wraps **Bootstrap 5.3.x**, which includes:

* **No jQuery dependency** - Bootstrap 5 uses vanilla JavaScript
* **New components** - Offcanvas sidebar navigation
* **Enhanced forms** - Floating labels and improved validation
* **Updated grid** - New XXL breakpoint for larger screens (≥1400px)
* **Modernized styling** - Cleaner CSS and improved utilities
* **Updated data attributes** - All Bootstrap data attributes now use `data-bs-*` prefix

## Migration from Bootstrap 4

See [migration-plan.md](migration-plan.md) for detailed migration steps from ph-oton-bootstrap4.

## License

Licensed under the Apache License, Version 2.0.

## News and Noteworthy

v0.9.2 - 2026-08-20
* Added new class `BootstrapFormSettings` that centrally manages the default grid specifications of `BootstrapForm` and `BootstrapViewForm`.
  It offers `getDefaultLeftGrid ()`, `getDefaultRightGrid ()`, `setDefaultLeftGrid (BootstrapGridSpec)` and `setDefaultSplitting (BootstrapGridSpec, BootstrapGridSpec)`
* Removed `BootstrapForm.DEFAULT_LEFT_PART`, `BootstrapForm.DEFAULT_RIGHT_PART`, `BootstrapViewForm.DEFAULT_LEFT_PART` and `BootstrapViewForm.DEFAULT_RIGHT_PART` in favour of `BootstrapFormSettings.DEFAULT_LEFT_PART`, `BootstrapFormSettings.DEFAULT_LEFT_GRID` and `BootstrapFormSettings.DEFAULT_RIGHT_GRID`
* `BootstrapViewForm` now uses the same default grid as `BootstrapForm` (`col-12 col-sm-2` for the left part instead of `col-3`)

v0.9.1 - 2026-08-19
* Added `BootstrapGridSpec.Builder` incl. the static factory methods `BootstrapGridSpec.builder ()` and `BootstrapGridSpec.builder (BootstrapGridSpec)` for easier creation of grid specifications
* Added `BootstrapGridSpec.getInverse ()` to create the complementary grid specification, so that two grid specifications add up to the maximum number of parts per breakpoint
* `BootstrapForm.setLeft (...)` and `BootstrapViewForm.setLeft (...)` now derive the right grid via `BootstrapGridSpec.getInverse ()`.
  For breakpoints that are not set at all (as in `setLeft (-1, -1, 3, 3, 2, 2)`) the right side now gets `col-12` instead of no grid class at all
* The `BootstrapDateTimePicker` calendar headline now shows the four digit year (e.g. "August 2026") instead of the Tempus Dominus default two digit year (e.g. "August 26"), as in the Bootstrap 4 version.
  Added `BootstrapDateTimePicker.dayViewHeaderFormat ()`, `setDayViewHeaderFormat (JSAssocArray)` and `createDefaultDayViewHeaderFormat ()` to customize the new `localization.dayViewHeaderFormat` option

v0.9.0 - 2026-08-19
* Initial release for Bootstrap 5.3.x
* Migration from Bootstrap 4 wrapper
* Full support for Bootstrap 5 components
* Updated for JakartaEE 10
* Reworked `BootstrapInputGroup` to use the flat Bootstrap 5 markup (no more `input-group-prepend`/`input-group-append` wrappers)
* Made the DateTimePicker based on Tempus Dominus v6.10.4 work: fixed initialization (`new tempusDominus.TempusDominus`), format token mapping, icon configuration and initial value handling; clicking into the input opens the picker as well (as in the Bootstrap 4 version)
* Added Popper v2.11.8 as a contained resource, as it is required by Tempus Dominus for popup positioning
* Migrated `BootstrapTooltip` and `BootstrapModal` from the removed jQuery plugin API to the native `bootstrap.Tooltip`/`bootstrap.Modal` JS API
* Horizontal form labels now use the `col-form-label` class for correct alignment
* Added new components: `BootstrapOffcanvas`, `BootstrapFormFloating` (floating labels) and `BootstrapValidFeedback`
* Added the missing `EXPAND_XXL` entry to `EBootstrapNavbarExpandType`
* Updated Tempus Dominus from v6.9.4 to v6.10.4 (keyboard navigation and ARIA improvements). The shipped files are unmodified upstream copies - the ph-oton specific integration is documented in `ph-oton-bootstrap5-uictrls/src/main/resources/external/tempusdominus/README.md`
* Added `BootstrapDateTimePicker.setKeyboardNavigation (...)` for the new Tempus Dominus `display.keyboardNavigation` option
* Added `EBootstrap5DateTimePickerTexts.TOGGLE_ARIA_LABEL` and fill the new Tempus Dominus `localization.toggleAriaLabel` key with it
* Added the demo page "Misc Controls" that shows all controls without a dedicated demo page (modal, offcanvas, tooltip, collapse, floating labels, validation feedback, cards, collapsible cards, alerts, badges, breadcrumb, list group, dropdown, tabs, tree view, file upload, Select2 and Prism)
* `ph-oton-bootstrap5-uictrls` now uses FontAwesome 6 (`ph-oton-icon-fontawesome6`) instead of FontAwesome 5.
  This affects `BootstrapDateTimePicker` (which now matches the Tempus Dominus default icon set), `BootstrapCardCollapsible` and `BootstrapSimpleTooltip` (`QUESTION_CIRCLE` became `CIRCLE_QUESTION`).
  FontAwesome 6 requires the style class in addition to the icon class, so `bootstrap-ext.css` now matches `.fa-solid` as well

---

My personal [Coding Styleguide](https://github.com/phax/meta/blob/master/CodingStyleguide.md) |
It is appreciated if you star the GitHub project if you like it.

