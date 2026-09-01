# Deployment

This service is a container. The root [Dockerfile](../../Dockerfile) is what we run in production. Pin a GHCR tag (`ghcr.io/nxtgrid/nxt-sts:vX.Y.Z`) or build from Git; both use that file.

HTTP **8080**. Health: `GET /actuator/health`. The process is stateless — no database. `POST /token` has **no API key**; do not put this service on the public internet.

| Platform | Guide |
|---|---|
| DigitalOcean App Platform | [digital-ocean-app-platform.md](./digital-ocean-app-platform.md) |
