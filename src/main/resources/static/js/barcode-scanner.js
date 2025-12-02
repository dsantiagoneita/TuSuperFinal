/**
 * TuSuper - Módulo de Escáner de Código de Barras USB
 * 
 * Este módulo captura la entrada de lectores de código de barras USB.
 * Los lectores USB funcionan como dispositivos HID (Human Interface Device)
 * que emulan un teclado, enviando caracteres rápidamente seguidos de Enter.
 * 
 * DEPENDENCIAS: Ninguna externa - utiliza JavaScript vanilla disponible en navegadores.
 * 
 * Características:
 * - Detección automática de entrada rápida (característica de lectores USB)
 * - Buffer de caracteres con timeout configurable
 * - Búsqueda asíncrona mediante API REST
 * - Feedback visual y sonoro
 * - Compatible con múltiples formatos de código de barras (EAN-8, EAN-13, UPC, etc.)
 * 
 * @author TuSuper Team
 * @version 1.0
 */

const BarcodeScanner = (function() {
    'use strict';

    // Configuración del escáner
    const config = {
        // Tiempo máximo entre caracteres para considerarse entrada de escáner (ms)
        // Los humanos escriben ~200ms entre teclas, los escáneres ~50ms o menos
        maxTimeBetweenChars: 100,
        
        // Tiempo de espera después del último carácter antes de procesar (ms)
        debounceTime: 150,
        
        // Longitud mínima del código de barras
        minCodeLength: 8,
        
        // Longitud máxima del código de barras
        maxCodeLength: 14,
        
        // URL de la API de búsqueda
        apiUrl: '/api/codigo-barras/buscar',
        
        // Habilitar sonidos
        enableSounds: true,
        
        // Habilitar logs de depuración
        debug: false
    };

    // Estado del escáner
    let state = {
        buffer: '',
        lastKeyTime: 0,
        debounceTimer: null,
        isProcessing: false,
        isEnabled: true,
        inputElement: null,
        resultCallback: null,
        errorCallback: null
    };

    /**
     * Log de depuración
     */
    function debugLog(...args) {
        if (config.debug) {
            console.log('[BarcodeScanner]', ...args);
        }
    }

    /**
     * Reproduce un sonido de feedback
     */
    function playSound(type) {
        if (!config.enableSounds) return;
        
        try {
            const audioContext = new (window.AudioContext || window.webkitAudioContext)();
            const oscillator = audioContext.createOscillator();
            const gainNode = audioContext.createGain();
            
            oscillator.connect(gainNode);
            gainNode.connect(audioContext.destination);
            
            if (type === 'success') {
                oscillator.frequency.value = 800;
                oscillator.type = 'sine';
                gainNode.gain.value = 0.3;
            } else if (type === 'error') {
                oscillator.frequency.value = 300;
                oscillator.type = 'square';
                gainNode.gain.value = 0.2;
            } else {
                oscillator.frequency.value = 600;
                oscillator.type = 'sine';
                gainNode.gain.value = 0.2;
            }
            
            oscillator.start();
            setTimeout(() => {
                oscillator.stop();
                audioContext.close();
            }, type === 'error' ? 300 : 150);
        } catch (e) {
            debugLog('Error reproduciendo sonido:', e);
        }
    }

    /**
     * Limpia el buffer de entrada
     */
    function clearBuffer() {
        state.buffer = '';
        if (state.debounceTimer) {
            clearTimeout(state.debounceTimer);
            state.debounceTimer = null;
        }
    }

    /**
     * Valida si el código tiene formato válido
     */
    function isValidBarcode(code) {
        if (!code || typeof code !== 'string') return false;
        
        const numericCode = code.replace(/[^0-9]/g, '');
        return numericCode.length >= config.minCodeLength && 
               numericCode.length <= config.maxCodeLength;
    }

    /**
     * Busca un producto por código de barras en la API
     */
    async function searchProduct(barcode) {
        if (state.isProcessing) {
            debugLog('Ya hay una búsqueda en proceso');
            return;
        }

        state.isProcessing = true;
        debugLog('Buscando producto:', barcode);

        try {
            const response = await fetch(`${config.apiUrl}?codigo=${encodeURIComponent(barcode)}`, {
                method: 'GET',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                credentials: 'same-origin'
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();
            debugLog('Respuesta API:', data);

            if (data.encontrado) {
                playSound('success');
                if (state.resultCallback) {
                    state.resultCallback(data.producto, barcode);
                }
            } else {
                playSound('error');
                if (state.errorCallback) {
                    state.errorCallback(data.mensaje, barcode);
                }
            }
        } catch (error) {
            console.error('[BarcodeScanner] Error en búsqueda:', error);
            playSound('error');
            if (state.errorCallback) {
                state.errorCallback('Error de conexión: ' + error.message, barcode);
            }
        } finally {
            state.isProcessing = false;
        }
    }

    /**
     * Procesa el buffer cuando se completa la entrada
     */
    function processBuffer() {
        const code = state.buffer.trim();
        clearBuffer();

        if (!code) {
            debugLog('Buffer vacío, ignorando');
            return;
        }

        debugLog('Procesando código:', code);

        if (isValidBarcode(code)) {
            searchProduct(code);
        } else {
            debugLog('Código inválido:', code);
            if (code.length > 0) {
                playSound('error');
                if (state.errorCallback) {
                    state.errorCallback(
                        `Código inválido: "${code}". Debe tener entre ${config.minCodeLength} y ${config.maxCodeLength} dígitos.`,
                        code
                    );
                }
            }
        }
    }

    /**
     * Manejador de eventos de teclado
     */
    function handleKeyPress(event) {
        if (!state.isEnabled) return;
        
        const currentTime = Date.now();
        const timeDiff = currentTime - state.lastKeyTime;
        
        // Si pasó mucho tiempo desde la última tecla, limpiar el buffer
        if (timeDiff > config.maxTimeBetweenChars && state.buffer.length > 0) {
            debugLog('Timeout entre caracteres, limpiando buffer');
            clearBuffer();
        }
        
        state.lastKeyTime = currentTime;

        // Si es Enter, procesar el buffer
        if (event.key === 'Enter') {
            event.preventDefault();
            
            if (state.debounceTimer) {
                clearTimeout(state.debounceTimer);
            }
            
            processBuffer();
            return;
        }

        // Solo agregar caracteres numéricos al buffer (los códigos de barras son numéricos)
        if (/^\d$/.test(event.key)) {
            state.buffer += event.key;
            debugLog('Buffer actual:', state.buffer);
            
            // Actualizar el input visual si existe
            if (state.inputElement) {
                state.inputElement.value = state.buffer;
            }
            
            // Reiniciar el timer de debounce
            if (state.debounceTimer) {
                clearTimeout(state.debounceTimer);
            }
            
            // Configurar timer para procesar si no llega más entrada
            state.debounceTimer = setTimeout(() => {
                if (state.buffer.length >= config.minCodeLength) {
                    processBuffer();
                }
            }, config.debounceTime);
        }
    }

    /**
     * Manejador específico para campos de entrada
     */
    function handleInputKeyPress(event) {
        // Para campos de entrada, usar Enter para búsqueda manual
        if (event.key === 'Enter') {
            event.preventDefault();
            const code = event.target.value.trim();
            
            if (code) {
                clearBuffer();
                
                if (isValidBarcode(code)) {
                    searchProduct(code);
                } else {
                    playSound('error');
                    if (state.errorCallback) {
                        state.errorCallback(
                            `Código inválido: "${code}". Debe tener entre ${config.minCodeLength} y ${config.maxCodeLength} dígitos.`,
                            code
                        );
                    }
                }
            }
        }
    }

    /**
     * Inicializa el escáner de código de barras
     * 
     * @param {Object} options - Opciones de configuración
     * @param {HTMLElement} options.inputElement - Elemento input para mostrar/escribir el código
     * @param {Function} options.onResult - Callback cuando se encuentra un producto
     * @param {Function} options.onError - Callback cuando hay un error o no se encuentra
     * @param {boolean} options.globalCapture - Si capturar eventos globalmente (default: true)
     */
    function init(options = {}) {
        debugLog('Inicializando escáner...');

        // Aplicar opciones
        if (options.inputElement) {
            state.inputElement = options.inputElement;
            state.inputElement.addEventListener('keypress', handleInputKeyPress);
        }

        state.resultCallback = options.onResult || null;
        state.errorCallback = options.onError || null;

        // Configuración adicional
        if (options.apiUrl) config.apiUrl = options.apiUrl;
        if (options.minCodeLength) config.minCodeLength = options.minCodeLength;
        if (options.maxCodeLength) config.maxCodeLength = options.maxCodeLength;
        if (options.debug !== undefined) config.debug = options.debug;
        if (options.enableSounds !== undefined) config.enableSounds = options.enableSounds;

        // Captura global de teclado para lectores USB
        if (options.globalCapture !== false) {
            document.addEventListener('keypress', handleKeyPress);
            debugLog('Captura global de teclado habilitada');
        }

        state.isEnabled = true;
        debugLog('Escáner inicializado correctamente');
    }

    /**
     * Destruye el escáner y limpia los event listeners
     */
    function destroy() {
        document.removeEventListener('keypress', handleKeyPress);
        
        if (state.inputElement) {
            state.inputElement.removeEventListener('keypress', handleInputKeyPress);
        }
        
        clearBuffer();
        state.isEnabled = false;
        state.resultCallback = null;
        state.errorCallback = null;
        state.inputElement = null;
        
        debugLog('Escáner destruido');
    }

    /**
     * Habilita/deshabilita el escáner
     */
    function setEnabled(enabled) {
        state.isEnabled = enabled;
        debugLog('Escáner', enabled ? 'habilitado' : 'deshabilitado');
    }

    /**
     * Realiza una búsqueda manual
     */
    function manualSearch(barcode) {
        if (isValidBarcode(barcode)) {
            searchProduct(barcode);
        } else {
            playSound('error');
            if (state.errorCallback) {
                state.errorCallback(
                    `Código inválido: "${barcode}". Debe tener entre ${config.minCodeLength} y ${config.maxCodeLength} dígitos.`,
                    barcode
                );
            }
        }
    }

    /**
     * Obtiene el estado actual del escáner
     */
    function getStatus() {
        return {
            isEnabled: state.isEnabled,
            isProcessing: state.isProcessing,
            bufferLength: state.buffer.length
        };
    }

    // API pública
    return {
        init,
        destroy,
        setEnabled,
        manualSearch,
        getStatus,
        clearBuffer,
        // Para pruebas
        _config: config,
        _state: state
    };
})();

// Exportar para uso con módulos si es necesario
if (typeof module !== 'undefined' && module.exports) {
    module.exports = BarcodeScanner;
}
