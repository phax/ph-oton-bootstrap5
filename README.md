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

## News and Noteworthy

**v1.0.0 - Work in Progress**
* Initial release for Bootstrap 5.3.x
* Migration from Bootstrap 4 wrapper
* Full support for Bootstrap 5 components
* Updated for JakartaEE 10

## Migration from Bootstrap 4

See [migration-plan.md](migration-plan.md) for detailed migration steps from ph-oton-bootstrap4.

## License

Licensed under the Apache License, Version 2.0.

---

My personal [Coding Styleguide](https://github.com/phax/meta/blob/master/CodingStyleguide.md) |
It is appreciated if you star the GitHub project if you like it.

