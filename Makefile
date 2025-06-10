.PHONY: help build up down logs clean dev dev-up dev-down

help:
	@echo "Comandos disponibles:"
	@echo "  make build    - Construir imágenes Docker"
	@echo "  make up       - Iniciar servicios en producción"
	@echo "  make down     - Detener servicios"
	@echo "  make logs     - Ver logs de los servicios"
	@echo "  make clean    - Limpiar imágenes y volúmenes"
	@echo "  make dev      - Construir para desarrollo"
	@echo "  make dev-up   - Iniciar servicios en desarrollo"
	@echo "  make dev-down - Detener servicios de desarrollo"

build:
	docker-compose build

up:
	docker-compose up -d

down:
	docker-compose down

logs:
	docker-compose logs -f

clean:
	docker-compose down -v
	docker system prune -af

dev:
	docker-compose -f docker-compose.dev.yml build

dev-up:
	docker-compose -f docker-compose.dev.yml up

dev-down:
	docker-compose -f docker-compose.dev.yml down