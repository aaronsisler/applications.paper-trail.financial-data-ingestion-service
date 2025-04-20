# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## Legend

* `Added` for new features.
* `Changed` for changes in existing functionality.
* `Deprecated` for soon-to-be removed features.
* `Removed` for now removed features.
* `Fixed` for any bug fixes.
* `Security` in case of vulnerabilities.

## [0.9.0] Account transaction queue test for integration testing

### Added

- Adding in the AWS Queue portion and listening to the output

## [0.8.0] Base of integration testing

### Added

- Adding in the pom, deps, and root application is up test

## [0.7.0] Account Transaction Publish to Queue

### Added

- Adding logic for publishing account transaction to queue

## [0.6.0] Account Transaction CSV invalid field validation

### Added

- Adding in the validation for transaction date for Account Transaction in the CSV

## [0.5.0] Account Transaction CSV invalid field validation

### Added

- Adding in the validation for description and amount for Account Transaction in the CSV

## [0.4.0] Account Transaction Contract cleanup

### Changed

- Needing to modify how the contracts are so the constructors are happy
- Adding in the shell for some invalid account transaction tests

## [0.3.0] Ingest Account Transaction

### Added

- Adding in Account Transaction controller for ingestion

## [0.2.0] Cucumber Base

### Added

- Cucumber and Swagger dependencies
- Info BUT

## [0.1.0] Base Repo

### Added

- Health Actuator
- Info Actuator
- Check Style