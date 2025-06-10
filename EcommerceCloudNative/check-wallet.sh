#!/bin/bash

echo "Verificando Wallet de Oracle..."

WALLET_DIR="./Wallet_EcommerceCloudNative"

if [ -d "$WALLET_DIR" ]; then
    echo "✓ Directorio Wallet encontrado"
    
    # Verificar archivos esenciales
    FILES=("cwallet.sso" "ewallet.p12" "sqlnet.ora" "tnsnames.ora")
    
    for file in "${FILES[@]}"; do
        if [ -f "$WALLET_DIR/$file" ]; then
            echo "✓ $file encontrado"
        else
            echo "✗ $file NO encontrado"
        fi
    done
else
    echo "✗ Directorio Wallet NO encontrado"
    echo "Por favor, asegúrate de que el Wallet esté en: $WALLET_DIR"
    exit 1
fi

echo "Verificación completada."