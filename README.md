# TFGFinder GEI API

TFG Finder backend developed by the GEI team. A Spring Boot project including Spring REST, HATEOAS, JPA, etc. Additional details: [HELP.md](HELP.md)

[![Open Issues](https://img.shields.io/github/issues-raw/UdL-EPS-SoftArch/tfgfinder-gei-api?logo=github)](https://github.com/orgs/UdL-EPS-SoftArch/projects/23)
[![CI/CD](https://github.com/UdL-EPS-SoftArch/tfgfinder-gei-api/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/UdL-EPS-SoftArch/tfgfinder-gei-api/actions)
[![CucumberReports: UdL-EPS-SoftArch](https://messages.cucumber.io/api/report-collections/faed8ca5-e474-4a1a-a72a-b8e2a2cd69f0/badge)](https://reports.cucumber.io/report-collections/faed8ca5-e474-4a1a-a72a-b8e2a2cd69f0)
[![Deployment status](https://img.shields.io/uptimerobot/status/m798592923-a2564376b6f4260b53e3463f)](https://tfgfinder-gei-api.fly.dev/users)

## Vision

**For** students, teachers and organisations **who** want to submit a final degree project proposal
**the project** TFGFinder **is a** web application
**that** allows them to offer TFG ideas, show interest in them and agree on participating in the proposal before it is submitted to the university.
**Unlike** the current approach based on sending e-mails back and forth.

## Features per Stakeholder

| USER                     | ADMIN                | Student           | Professor       | Organisation    |
|--------------------------|----------------------|-------------------|-----------------|-----------------|
| Register as Organisation | Approve Organisation | Propose           | Propose         | Propose         |
| Login                    | Reject Organisation  | Browse            | Browse          | Browse          |
| Logout                   | Reject Offer         | Search            | Search          | Search          |
| View Profile             | Add Category         | Show Interest     | Show Interest   | Show Interest   |
| Edit Profile             | Edit Category        | Accept Interest   | Accept Interest | Accept Interest |
|                          | Delete Category      | Reject Interest   | Reject Interest | Reject Interest |
|                          |                      | Agree             | Agree           | Agree           |
|                          |                      | Update Proposal   | Update Proposal | Update Proposal |
|                          |                      | Download Proposal |                 |                 |
|                          |                      | Favorite          | Favorite        | Favorite        |
|                          |                      | Invite            | Invite          | Invite          |
|                          |                      | Reject Invite     | Reject Invite   | Reject Invite   |
|                          |                      |                   |                 |                 |

## Entities Model

![EntityModelsDiagram](https://www.plantuml.com/plantuml/svg/5Sqn3i8m3030dLV00rBlJ6Ne1X9InGD2x3IM4YUo7V5zOBhnLlEeFbizNLXIdKXJwxcu3xi2QFmi1tkPxscagOkJ-99wWnVUm_RSmzwptpGLy7na5YGD1t68Wq4f6nIAQD0Y2spYx4psw4Xr6Vzp9S6VNm00?v2)

