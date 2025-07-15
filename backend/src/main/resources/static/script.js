/**
 * @file Script principal para el dashboard de CAMPITO.
 * @description Maneja la lógica de la interfaz, interacciones de usuario, y visualización de datos.
 * @author Gemini AI
 * @version 1.0.1
 */

// Módulo principal de la aplicación (IIFE)
(function() {
    "use strict";

    const appState = {
        transactions: [],
        reasons: [],
        contacts: [],
        budgets: [],
        workspaces: [
            { id: 1, name: 'Campo en Guadalupe Norte' },
            { id: 2, name: 'Campo Nuevo' },
        ],
        charts: {
            monthly: null,
            expenses: null,
            balanceTrend: null
        },
        lastSearchResults: [],
        currentBalance: 0,
    };

    const DOMElements = {
        transactionModal: document.getElementById('transactionModal'),
        searchModal: document.getElementById('searchModal'),
        budgetModal: document.getElementById('budgetModal'),
        workspaceModal: document.getElementById('workspaceModal'),
        shareModal: document.getElementById('shareModal'),
        userMenu: document.getElementById('userMenu'), // Menú de usuario
        shareWorkspaceSelect: document.getElementById('shareWorkspaceSelect'),
        transactionForm: document.getElementById('transactionForm'),
        searchForm: document.getElementById('searchForm'),
        budgetForm: document.getElementById('budgetForm'),
        workspaceForm: document.getElementById('workspaceForm'),
        shareForm: document.getElementById('shareForm'),
        newReasonForm: document.getElementById('newReasonForm'),
        newContactForm: document.getElementById('newContactForm'),
        reasonSelect: document.getElementById('reason'),
        contactSelect: document.getElementById('recipient'),
        searchReasonSelect: document.getElementById('searchReason'),
        searchContactSelect: document.getElementById('searchRecipient'),
        searchYearSelect: document.getElementById('searchYear'),
        budgetReasonSelect: document.getElementById('budgetReason'),
        newReasonInput: document.getElementById('newReasonInput'),
        newContactInput: document.getElementById('newContactInput'),
        orderBySelect: document.getElementById('orderBy'),
        recentTransactionsList: document.getElementById('recentTransactionsList'),
        searchResultsContainer: document.getElementById('searchResults'),
        alertsListContainer: document.getElementById('alertsList'),
        notificationContainer: document.getElementById('notification-container'),
        balanceAmount: document.querySelector('.balance__value'),
        balanceContainer: document.querySelector('.balance__amount'),
        balanceDate: document.querySelector('.balance__date'),
    };

    function initializeApp() {
        loadSampleData();
        setupEventListeners();
        updateDashboard();
        populateYearSelector();
        loadAuthenticatedUser();
    }

    function loadAuthenticatedUser() {
        fetch('/usuario/me')
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error('No se pudo obtener la información del usuario.');
            })
            .then(usuario => {
                if (usuario) {
                    document.querySelector('.user-menu__username').textContent = usuario.nombre;
                    document.querySelector('.user-menu__email').textContent = usuario.email;
                    appState.authenticatedUserId = usuario.id; // Guardar el ID del usuario
                    loadWorkspacesForUser(appState.authenticatedUserId); // Cargar espacios de trabajo
                }
            })
            .catch(error => {
                console.error('Error al cargar datos del usuario:', error);
                // Opcional: redirigir al login si no se pueden obtener los datos
                // window.location.href = '/login.html';
            });
    }

    function loadWorkspacesForUser(userId) {
        if (!userId) {
            console.warn('No se puede cargar espacios de trabajo: ID de usuario no disponible.');
            return;
        }
        fetch(`/espaciotrabajo/listar/${userId}`)
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error('No se pudieron cargar los espacios de trabajo.');
            })
            .then(workspaces => {
                appState.workspaces = workspaces;
                populateShareWorkspaceSelect(); // Actualizar el selector de compartir
                populateMainWorkspaceSelect(); // Actualizar el selector principal del dashboard
            })
            .catch(error => {
                console.error('Error al cargar espacios de trabajo:', error);
                showNotification('Error al cargar los espacios de trabajo', 'error');
            });
    }

    function populateMainWorkspaceSelect() {
        const selectElement = document.getElementById('workspaceSelect');
        if (!selectElement) return;

        selectElement.innerHTML = '<option value="">Seleccionar espacio de trabajo</option>';
        appState.workspaces.forEach(workspace => {
            const option = document.createElement('option');
            option.value = workspace.id;
            option.textContent = workspace.nombre;
            selectElement.appendChild(option);
        });
    }

    function loadSampleData() {
        appState.transactions = [
            { id: 1, type: 'income', amount: 50000, reason: 'Venta de ganado', recipient: 'Juan Pérez', description: 'Venta de 10 novillos', date: '2024-05-15' },
            { id: 2, type: 'expense', amount: 15000, reason: 'Alimento para ganado', recipient: 'Proveedor ABC', description: 'Compra de alimento', date: '2024-05-20' },
            { id: 3, type: 'income', amount: 30000, reason: 'Venta de leche', recipient: 'Lácteos del Sur', description: 'Entrega de leche', date: '2024-05-28' },
            { id: 4, type: 'expense', amount: 8000, reason: 'Veterinario', recipient: 'Dr. García', description: 'Vacunación', date: '2024-06-10' },
            { id: 5, type: 'expense', amount: 25000, reason: 'Mantenimiento', recipient: 'Taller Rural', description: 'Arreglo de tractor', date: '2024-06-18' },
            { id: 6, type: 'income', amount: 32000, reason: 'Venta de leche', recipient: 'Lácteos del Sur', description: 'Entrega de leche', date: '2024-06-28' },
            { id: 7, type: 'income', amount: 75000, reason: 'Venta de ganado', recipient: 'Juan Pérez', description: 'Venta de 15 terneros', date: '2024-07-12' },
            { id: 8, type: 'expense', amount: 18000, reason: 'Alimento para ganado', recipient: 'Proveedor ABC', description: 'Compra de alimento', date: '2024-07-22' },
            { id: 9, type: 'income', amount: 31000, reason: 'Venta de leche', recipient: 'Lácteos del Sur', description: 'Entrega de leche', date: '2024-07-28' },
            { id: 10, type: 'expense', amount: 12000, reason: 'Salarios', recipient: 'Personal', description: 'Pago de quincena', date: '2024-08-15' },
            { id: 11, type: 'expense', amount: 45000, reason: 'Compra de equipos', recipient: 'Maquinaria SA', description: 'Compra de sembradora', date: '2024-08-25' },
            { id: 12, type: 'income', amount: 33000, reason: 'Venta de leche', recipient: 'Lácteos del Sur', description: 'Entrega de leche', date: '2024-08-28' },
            { id: 13, type: 'expense', amount: 9000, reason: 'Impuestos', recipient: 'AFIP', description: 'Pago de impuestos', date: '2024-09-20' },
            { id: 14, type: 'income', amount: 34000, reason: 'Venta de leche', recipient: 'Lácteos del Sur', description: 'Entrega de leche', date: '2024-09-28' },
            { id: 15, type: 'income', amount: 120000, reason: 'Venta de ganado', recipient: 'Juan Pérez', description: 'Venta de 20 novillos', date: '2024-10-18' },
            { id: 16, type: 'expense', amount: 20000, reason: 'Alimento para ganado', recipient: 'Proveedor ABC', description: 'Compra de alimento', date: '2024-10-25' },
            { id: 17, type: 'income', amount: 35000, reason: 'Venta de leche', recipient: 'Lácteos del Sur', description: 'Entrega de leche', date: '2024-10-28' }
        ];
    }

    function setupEventListeners() {
        document.getElementById('newTransactionBtn').addEventListener('click', () => toggleModal('transactionModal', true));
        document.getElementById('searchTransactionsBtn').addEventListener('click', () => toggleModal('searchModal', true));
        document.getElementById('setupAlertsBtn').addEventListener('click', () => toggleModal('budgetModal', true));
        document.getElementById('newWorkspaceBtn').addEventListener('click', () => toggleModal('workspaceModal', true));
        document.getElementById('shareWorkspaceBtn').addEventListener('click', () => {
            toggleModal('shareModal', true);
            populateShareWorkspaceSelect(); // Populate the dropdown when opening the modal
        });

        document.getElementById('userMenuBtn').addEventListener('click', () => toggleUserMenu());

        // Listener para el selector de espacio de trabajo principal
        const workspaceSelect = document.getElementById('workspaceSelect');
        if (workspaceSelect) {
            workspaceSelect.addEventListener('change', (event) => {
                const idEspacioTrabajo = event.target.value;
                if (idEspacioTrabajo) {
                    const selectedWorkspace = appState.workspaces.find(ws => ws.id == idEspacioTrabajo);
                    if (selectedWorkspace) {
                        appState.currentBalance = selectedWorkspace.saldo;
                        updateBalance();
                    }
                    cargarMotivos(idEspacioTrabajo);
                    cargarContactos(idEspacioTrabajo);
                } else {
                    appState.currentBalance = 0;
                    updateBalance();
                    clearMotivos();
                    clearContactos();
                }
            });
        }

        document.getElementById('closeTransactionModalBtn').addEventListener('click', () => toggleModal('transactionModal', false));
        document.getElementById('closeSearchModalBtn').addEventListener('click', () => toggleModal('searchModal', false));
        document.getElementById('closeBudgetModalBtn').addEventListener('click', () => toggleModal('budgetModal', false));
        document.getElementById('closeWorkspaceModalBtn').addEventListener('click', () => toggleModal('workspaceModal', false));
        document.getElementById('closeShareModalBtn').addEventListener('click', () => toggleModal('shareModal', false));
        document.getElementById('cancelTransactionBtn').addEventListener('click', () => toggleModal('transactionModal', false));
        document.getElementById('cancelBudgetBtn').addEventListener('click', () => toggleModal('budgetModal', false));
        document.getElementById('cancelWorkspaceBtn').addEventListener('click', () => toggleModal('workspaceModal', false));
        document.getElementById('cancelShareBtn').addEventListener('click', () => toggleModal('shareModal', false));

        // Envíos de formularios
        DOMElements.transactionForm.addEventListener('submit', handleTransactionSubmit);
        DOMElements.searchForm.addEventListener('submit', handleSearchSubmit);
        DOMElements.budgetForm.addEventListener('submit', handleBudgetSubmit);
        DOMElements.workspaceForm.addEventListener('submit', handleWorkspaceSubmit);
        DOMElements.shareForm.addEventListener('submit', handleShareSubmit);
        document.getElementById('newReasonBtn').addEventListener('click', () => toggleNestedForm('newReasonForm', true));
        document.getElementById('cancelReasonBtn').addEventListener('click', () => toggleNestedForm('newReasonForm', false));
        document.getElementById('saveReasonBtn').addEventListener('click', handleNewReason);
        document.getElementById('newContactBtn').addEventListener('click', () => toggleNestedForm('newContactForm', true));
        document.getElementById('cancelContactBtn').addEventListener('click', () => toggleNestedForm('newContactForm', false));
        document.getElementById('saveContactBtn').addEventListener('click', handleNewContact);
        document.getElementById('clearSearchBtn').addEventListener('click', clearSearch);
        document.getElementById('viewAllTransactionsBtn').addEventListener('click', showAllTransactions);
        DOMElements.orderBySelect.addEventListener('change', handleSearchOrderChange);
        window.addEventListener('click', (event) => {
            if (event.target.classList.contains('modal')) {
                toggleModal(event.target.id, false);
            }
            // Cierra el menú de usuario si se hace clic fuera de él
            if (!DOMElements.userMenu.hidden && !DOMElements.userMenu.contains(event.target) && !document.getElementById('userMenuBtn').contains(event.target)) {
                toggleUserMenu(false);
            }
        });
    }

    /**
     * Muestra u oculta el menú desplegable del usuario.
     * @param {boolean} [forceShow] - Fuerza la visualización o el ocultamiento del menú.
     */
    function toggleUserMenu(forceShow) {
        const isHidden = DOMElements.userMenu.hidden;
        DOMElements.userMenu.hidden = forceShow !== undefined ? !forceShow : !isHidden;
    }

    function updateDashboard() {
        updateBalance();
        renderRecentTransactions();
        initializeCharts();
        populateAllSelectors();
    }

    function updateBalance() {
        const balance = appState.currentBalance;
        DOMElements.balanceAmount.textContent = balance.toLocaleString('es-AR');
        DOMElements.balanceContainer.className = `balance__amount ${balance >= 0 ? 'balance__amount--positive' : 'balance__amount--negative'}`;
        DOMElements.balanceDate.textContent = `Actualizado: ${new Date().toLocaleDateString('es-AR')}`;
    }

    function renderRecentTransactions() {
        const container = DOMElements.recentTransactionsList;
        const recent = appState.transactions.slice(0, 5);
        renderTransactionList(container, recent);
    }

    function renderTransactionList(container, transactions) {
        container.innerHTML = '';
        if (transactions.length === 0) {
            container.innerHTML = '<p class="transactions-list__empty">No se encontraron transacciones.</p>';
            return;
        }
        transactions.forEach(transaction => {
            const item = createTransactionElement(transaction);
            container.appendChild(item);
        });
    }

    function createTransactionElement(transaction) {
        const item = document.createElement('div');
        item.className = 'transaction-item';
        item.dataset.transactionId = transaction.id;
        const isIncome = transaction.type === 'income';
        item.innerHTML = `
            <div class="transaction-item__info">
                <div class="transaction-item__details">
                    <span class="transaction-item__reason">${transaction.reason}</span>
                    <span class="transaction-item__meta">${new Date(transaction.date).toLocaleDateString('es-AR')} &bull; ${transaction.recipient || 'N/A'}</span>
                </div>
            </div>
            <div class="transaction-item__amount ${isIncome ? 'transaction-item__amount--income' : 'transaction-item__amount--expense'}">
                ${isIncome ? '+' : '-'}${transaction.amount.toLocaleString('es-AR')}
            </div>
        `;
        item.addEventListener('click', () => showTransactionDetails(transaction));
        return item;
    }

    function getTransactionIcon(reason) {
        const iconMap = {
            'Venta de ganado': 'fas fa-cow',
            'Venta de leche': 'fas fa-wine-bottle',
            'Alimento para ganado': 'fas fa-seedling',
            'Veterinario': 'fas fa-stethoscope',
            'Compra de equipos': 'fas fa-tools',
            'Mantenimiento': 'fas fa-wrench',
            'Transporte': 'fas fa-truck',
            'Impuestos': 'fas fa-file-invoice-dollar',
            'Salarios': 'fas fa-users',
        };
        return iconMap[reason] || 'fas fa-ellipsis-h';
    }

    function initializeCharts() {
        Object.values(appState.charts).forEach(chart => chart?.destroy());
        createMonthlyChart();
        createExpensesChart();
        createBalanceTrendChart();
    }

    function createMonthlyChart() {
        const ctx = document.getElementById('monthlyChart').getContext('2d');
        const data = { labels: ['Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'], datasets: [ { label: 'Ingresos', data: [45000, 52000, 48000, 55000, 50000, 80000], backgroundColor: '#3B82F6' }, { label: 'Gastos', data: [35000, 38000, 42000, 40000, 45000, 43000], backgroundColor: '#03324F' } ] };
        appState.charts.monthly = new Chart(ctx, { type: 'bar', data, options: getChartOptions() });
    }

    function createExpensesChart() {
        const ctx = document.getElementById('expensesChart').getContext('2d');
        const expenseData = appState.transactions.filter(t => t.type === 'expense').reduce((acc, t) => {
            acc[t.reason] = (acc[t.reason] || 0) + t.amount;
            return acc;
        }, {});

        const numCategories = Object.keys(expenseData).length;
        if (numCategories === 0) return;

        const generatedColors = generateBluePalette(numCategories);

        const data = { 
            labels: Object.keys(expenseData), 
            datasets: [{ 
                data: Object.values(expenseData), 
                backgroundColor: generatedColors
            }] 
        };
        appState.charts.expenses = new Chart(ctx, { type: 'doughnut', data, options: getChartOptions('doughnut') });
    }

    /**
     * Genera una paleta de colores en una gama de azules y celestes.
     * @param {number} count - El número de colores a generar.
     * @returns {string[]} Un array de colores en formato HSL.
     */
    function generateBluePalette(count) {
        const colors = [];
        const baseHue = 210; // Tono base para el azul
        const saturation = 75; // Saturación constante para colores vibrantes
        const startLightness = 30; // Comienza con un azul oscuro
        const endLightness = 85;   // Termina con un celeste claro
        const step = count > 1 ? (endLightness - startLightness) / (count - 1) : 0;

        for (let i = 0; i < count; i++) {
            const lightness = startLightness + (i * step);
            colors.push(`hsl(${baseHue}, ${saturation}%, ${lightness}%)`);
        }
        return colors;
    }

    function createBalanceTrendChart() {
        const ctx = document.getElementById('balanceTrendChart').getContext('2d');
        const sorted = [...appState.transactions].sort((a, b) => new Date(a.date) - new Date(b.date));
        let accumulated = 0;
        const trendData = sorted.map(t => {
            accumulated += t.type === 'income' ? t.amount : -t.amount;
            return { x: t.date, y: accumulated };
        });
        if (trendData.length === 0) return;
        const data = { datasets: [{ label: 'Saldo Acumulado', data: trendData, borderColor: '#03324F', tension: 0.4, fill: true, backgroundColor: 'rgba(3, 50, 79, 0.1)' }] };
        appState.charts.balanceTrend = new Chart(ctx, { type: 'line', data, options: getChartOptions('line') });
    }

    function getChartOptions(type = 'bar') {
        const options = {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { position: type === 'doughnut' ? 'bottom' : 'top' },
                tooltip: { 
                    callbacks: { 
                        label: (c) => `${c.dataset.label || c.label}: ${(c.raw.y || c.raw).toLocaleString('es-AR')}` 
                    } 
                }
            }
        };
        if (type === 'line') {
            options.scales = {
                x: {
                    type: 'time',
                    time: {
                        unit: 'day',
                        tooltipFormat: 'dd MMM yyyy'
                    },
                    title: {
                        display: true,
                        text: 'Fecha'
                    }
                },
                y: {
                    title: {
                        display: true,
                        text: 'Saldo Acumulado ($)'
                    }
                }
            };
        }
        return options;
    }

    function toggleModal(modalId, show) {
        const modal = document.getElementById(modalId);
        if (show) {
            modal.hidden = false;
            if (modalId === 'transactionModal') DOMElements.transactionForm.reset();
            if (modalId === 'searchModal') DOMElements.searchForm.reset();
            if (modalId === 'budgetModal') DOMElements.budgetForm.reset();
        } else {
            modal.hidden = true;
        }
    }

    function toggleNestedForm(formId, show) {
        const form = document.getElementById(formId);
        form.hidden = !show;
        if (!show) {
            const input = form.querySelector('input');
            if (input) input.value = '';
        }
    }

    function handleTransactionSubmit(event) {
        event.preventDefault();
        const formData = new FormData(event.target);
        const newTransaction = {
            id: Date.now(),
            type: formData.get('transactionType'),
            amount: parseFloat(formData.get('amount')),
            reason: formData.get('reason'),
            recipient: formData.get('recipient'),
            description: formData.get('description'),
            date: formData.get('transactionDate'),
        };
        appState.transactions.unshift(newTransaction);
        updateDashboard();
        toggleModal('transactionModal', false);
        showNotification('Transacción guardada con éxito', 'success');
    }

    function handleSearchSubmit(event) {
        event.preventDefault();
        // Lógica de búsqueda deshabilitada temporalmente
        alert('La funcionalidad de búsqueda se conectará con el backend más adelante.');
    }

    function handleWorkspaceSubmit(event) {
        event.preventDefault();
        const workspaceNameInput = document.getElementById('workspaceName');
        const workspaceName = workspaceNameInput.value.trim();
        const errorElement = document.getElementById('workspaceNameError');

        if (workspaceName) {
            errorElement.hidden = true;
            const workspaceData = {
                nombre: workspaceName,
                idUsuarioAdmin: appState.authenticatedUserId
            };

            console.log('Enviando datos:', JSON.stringify(workspaceData));

            fetch('http://localhost:8080/espaciotrabajo/registrar', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(workspaceData)
            })
            .then(response => {
                if (!response.ok) {
                    // Si la respuesta no es OK, lanzamos un error para que lo capture el .catch
                    throw new Error('Error en la solicitud al servidor');
                }
                // No es necesario procesar un cuerpo JSON si la respuesta es exitosa pero vacía
            })
            .then(() => {
                // Limpiar el campo de texto
                workspaceNameInput.value = '';
                // Cerrar el modal
                toggleModal('workspaceModal', false);
                // Mostrar notificación de éxito
                showNotification(`Espacio "${workspaceName}" creado con éxito`, 'success');
                // Actualizar la lista de espacios de trabajo en la UI
                loadWorkspacesForUser(appState.authenticatedUserId); 
            })
            .catch(error => {
                console.error('Error:', error);
                showNotification('Error al crear el espacio de trabajo', 'error');
            });
        } else {
            errorElement.hidden = false;
            showNotification('Por favor, ingrese un nombre para el espacio de trabajo', 'error');
        }
    }

    function handleShareSubmit(event) {
        event.preventDefault();
        const workspaceId = DOMElements.shareWorkspaceSelect.value;
        const emailInput = document.getElementById('shareEmail');
        const email = emailInput.value.trim();

        if (!workspaceId) {
            showNotification('Por favor, seleccione un espacio de trabajo.', 'error');
            return;
        }

        if (!email) {
            showNotification('Por favor, ingrese un email válido.', 'error');
            return;
        }

        if (email.length > 100) {
            showNotification('El email no puede exceder los 100 caracteres.', 'error');
            return;
        }

        if (!appState.authenticatedUserId) {
            showNotification('Error: ID de usuario autenticado no disponible.', 'error');
            console.error('Error: appState.authenticatedUserId es null o undefined.');
            return;
        }

        const idUsuarioAdmin = appState.authenticatedUserId;

        fetch(`http://localhost:8080/espaciotrabajo/compartir/${email}/${workspaceId}/${idUsuarioAdmin}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            // No se envía body ya que los datos van en la URL
        })
        .then(response => {
            if (response.ok) {
                // Limpiar campos y cerrar modal
                DOMElements.shareWorkspaceSelect.value = '';
                emailInput.value = '';
                toggleModal('shareModal', false);
                showNotification(`Invitación enviada a ${email} para el espacio de trabajo.`, 'success');
            } else {
                throw new Error('Error al compartir el espacio de trabajo.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification('Error al compartir el espacio de trabajo.', 'error');
        });
    }

    function handleBudgetSubmit(event) {
        event.preventDefault();
        const formData = new FormData(event.target);
        const newBudget = {
            id: Date.now(),
            reason: formData.get('budgetReason'),
            threshold: parseFloat(formData.get('budgetThreshold')),
        };
        appState.budgets.push(newBudget);
        renderAlertsList();
        toggleModal('budgetModal', false);
        showNotification('Alerta de presupuesto guardada', 'success');
    }

    function populateAllSelectors() {
        // Ya no se necesita poblar los selectores de contactos desde aquí
    }

    function populateShareWorkspaceSelect() {
        const selectElement = DOMElements.shareWorkspaceSelect;
        selectElement.innerHTML = '<option value="">Seleccionar espacio de trabajo</option>';
        appState.workspaces.forEach(workspace => {
            const option = document.createElement('option');
            option.value = workspace.id;
            option.textContent = workspace.nombre;
            selectElement.appendChild(option);
        });
    }

    function populateSelector(selectElement, options, placeholder) {
        selectElement.innerHTML = `<option value="">${placeholder}</option>`;
        options.forEach(option => {
            const opt = document.createElement('option');
            opt.value = option;
            opt.textContent = option;
            selectElement.appendChild(opt);
        });
    }

    function cargarMotivos(idEspacioTrabajo) {
        fetch(`/transaccion/motivo/listar/${idEspacioTrabajo}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('No se pudieron cargar los motivos.');
                }
                return response.json();
            })
            .then(motivos => {
                appState.reasons = motivos;
                populateMotivoSelector(DOMElements.reasonSelect, 'Seleccionar motivo');
                populateMotivoSelector(DOMElements.searchReasonSelect, 'Todos los motivos');
                populateMotivoSelector(DOMElements.budgetReasonSelect, 'Seleccionar motivo');
            })
            .catch(error => {
                console.error('Error al cargar motivos:', error);
                showNotification('Error al cargar los motivos', 'error');
            });
    }

    function clearMotivos() {
        appState.reasons = [];
        populateMotivoSelector(DOMElements.reasonSelect, 'Seleccionar motivo');
        populateMotivoSelector(DOMElements.searchReasonSelect, 'Todos los motivos');
        populateMotivoSelector(DOMElements.budgetReasonSelect, 'Seleccionar motivo');
    }

    function populateMotivoSelector(selectElement, placeholder) {
        selectElement.innerHTML = `<option value="">${placeholder}</option>`;
        appState.reasons.forEach(motivo => {
            const opt = document.createElement('option');
            opt.value = motivo.id; // Usar el ID del motivo como valor
            opt.textContent = motivo.motivo; // Mostrar el nombre del motivo
            selectElement.appendChild(opt);
        });
    }

    function addReasonToSelectors(reason) {
        appState.reasons.push(reason);
        const option = document.createElement('option');
        option.value = reason.id;
        option.textContent = reason.motivo;
        DOMElements.reasonSelect.appendChild(option.cloneNode(true));
        DOMElements.searchReasonSelect.appendChild(option.cloneNode(true));
        DOMElements.budgetReasonSelect.appendChild(option.cloneNode(true));
    }

    function cargarContactos(idEspacioTrabajo) {
        fetch(`/transaccion/contacto/listar/${idEspacioTrabajo}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('No se pudieron cargar los contactos.');
                }
                return response.json();
            })
            .then(contactos => {
                appState.contacts = contactos;
                populateContactoSelector(DOMElements.contactSelect, 'Seleccionar contacto');
                populateContactoSelector(DOMElements.searchContactSelect, 'Todos los contactos');
            })
            .catch(error => {
                console.error('Error al cargar contactos:', error);
                showNotification('Error al cargar los contactos', 'error');
            });
    }

    function clearContactos() {
        appState.contacts = [];
        populateContactoSelector(DOMElements.contactSelect, 'Seleccionar contacto');
        populateContactoSelector(DOMElements.searchContactSelect, 'Todos los contactos');
    }

    function populateContactoSelector(selectElement, placeholder) {
        selectElement.innerHTML = `<option value="">${placeholder}</option>`;
        appState.contacts.forEach(contacto => {
            const opt = document.createElement('option');
            opt.value = contacto.id;
            opt.textContent = contacto.nombre;
            selectElement.appendChild(opt);
        });
    }

    function addContactToSelectors(contact) {
        appState.contacts.push(contact);
        const option = document.createElement('option');
        option.value = contact.id;
        option.textContent = contact.nombre;
        DOMElements.contactSelect.appendChild(option.cloneNode(true));
        DOMElements.searchContactSelect.appendChild(option.cloneNode(true));
    }

    function handleNewReason() {
        const newReason = DOMElements.newReasonInput.value.trim();
        const idEspacioTrabajo = document.getElementById('workspaceSelect').value;

        if (!idEspacioTrabajo) {
            showNotification('Por favor, seleccione un espacio de trabajo primero', 'error');
            return;
        }

        if (newReason) {
            const motivoData = {
                motivo: newReason,
                idEspacioTrabajo: idEspacioTrabajo
            };

            fetch('/transaccion/motivo/registrar', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(motivoData)
            })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error('Error al registrar el motivo.');
            })
            .then(motivoGuardado => {
                toggleNestedForm('newReasonForm', false);
                showNotification('Motivo guardado con éxito', 'success');
                addReasonToSelectors(motivoGuardado);
                DOMElements.reasonSelect.value = motivoGuardado.id;
            })
            .catch(error => {
                console.error('Error:', error);
                showNotification('Error al guardar el motivo', 'error');
            });
        } else {
            showNotification('Por favor, ingrese un nombre para el motivo', 'error');
        }
    }

    function handleNewContact() {
        const newContactName = DOMElements.newContactInput.value.trim();
        const idEspacioTrabajo = document.getElementById('workspaceSelect').value;

        if (!idEspacioTrabajo) {
            showNotification('Por favor, seleccione un espacio de trabajo primero', 'error');
            return;
        }

        if (newContactName) {
            const contactoData = {
                nombre: newContactName,
                idEspacioTrabajo: idEspacioTrabajo
            };

            fetch('/transaccion/contacto/registrar', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(contactoData)
            })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error('Error al registrar el contacto.');
            })
            .then(contactoGuardado => {
                toggleNestedForm('newContactForm', false);
                showNotification('Contacto guardado con éxito', 'success');
                addContactToSelectors(contactoGuardado);
                DOMElements.contactSelect.value = contactoGuardado.id;
            })
            .catch(error => {
                console.error('Error:', error);
                showNotification('Error al guardar el contacto', 'error');
            });
        } else {
            showNotification('Por favor, ingrese un nombre para el contacto', 'error');
        }
    }

    function renderAlertsList() {
        const container = DOMElements.alertsListContainer;
        container.innerHTML = '';
        if (appState.budgets.length === 0) {
            container.innerHTML = '<p>No hay alertas configuradas.</p>';
            return;
        }
        appState.budgets.forEach(budget => {
            const item = document.createElement('div');
            item.className = 'alert-item';
            item.innerHTML = `
                <span class="alert-item__info">
                    ${budget.reason}:
                    <span class="alert-item__threshold">$${budget.threshold.toLocaleString('es-AR')}</span>
                </span>
                <button class="alert-item__delete-btn" data-id="${budget.id}"><i class="fas fa-trash"></i></button>
            `;
            item.querySelector('.alert-item__delete-btn').addEventListener('click', (e) => {
                const id = parseInt(e.currentTarget.dataset.id);
                appState.budgets = appState.budgets.filter(b => b.id !== id);
                renderAlertsList();
            });
            container.appendChild(item);
        });
    }

    function clearSearch() {
        DOMElements.searchForm.reset();
        DOMElements.searchResultsContainer.innerHTML = '';
        appState.lastSearchResults = [];
    }

    function showAllTransactions() {
        appState.lastSearchResults = [...appState.transactions];
        applySearchOrder();
        toggleModal('searchModal', true);
    }

    function handleSearchOrderChange() {
        applySearchOrder();
    }

    function applySearchOrder() {
        const order = DOMElements.orderBySelect.value;
        let sorted = [...appState.lastSearchResults];
        switch (order) {
            case 'date-asc': sorted.sort((a, b) => new Date(a.date) - new Date(b.date)); break;
            case 'date-desc': sorted.sort((a, b) => new Date(b.date) - new Date(a.date)); break;
            case 'amount-asc': sorted.sort((a, b) => a.amount - b.amount); break;
            case 'amount-desc': sorted.sort((a, b) => b.amount - a.amount); break;
        }
        renderTransactionList(DOMElements.searchResultsContainer, sorted);
    }

    function showNotification(message, type = 'info') {
        const notification = document.createElement('div');
        notification.className = `notification notification--${type}`;
        notification.textContent = message;
        DOMElements.notificationContainer.appendChild(notification);
        setTimeout(() => {
            notification.remove();
        }, 3000);
    }

    function showTransactionDetails(transaction) {
        const details = `
            Tipo: ${transaction.type}
            Monto: ${transaction.amount.toLocaleString('es-AR')}
            Motivo: ${transaction.reason}
            Fecha: ${new Date(transaction.date).toLocaleDateString('es-AR')}
            Contacto: ${transaction.recipient || 'N/A'}
            Descripción: ${transaction.description || 'N/A'}
        `;
        alert(`Detalles de la Transacción:\n\n${details}`);
    }

    /**
     * Rellena el selector de años en el formulario de búsqueda.
     */
    function populateYearSelector() {
        // Se busca el elemento directamente aquí para asegurar que el DOM esté cargado.
        const selectElement = document.getElementById('searchYear');
        if (!selectElement) {
            console.error("El elemento select con id 'searchYear' no fue encontrado.");
            return;
        }

        const currentYear = new Date().getFullYear();
        const startYear = 2000;
        const endYear = 2030;

        // Limpiar opciones existentes (excepto la primera "Todos")
        while (selectElement.options.length > 1) {
            selectElement.remove(1);
        }

        for (let year = endYear; year >= startYear; year--) {
            const option = document.createElement('option');
            option.value = year;
            option.textContent = year;
            selectElement.appendChild(option);
        }
        
        selectElement.value = currentYear;
    }

    document.addEventListener('DOMContentLoaded', () => {
        if (document.getElementById('loginForm')) {
            initializeLoginPage();
        } else {
            initializeApp();
        }
    });

    function initializeLoginPage() {
        const loginForm = document.getElementById('loginForm');
        if (loginForm) {
            loginForm.addEventListener('submit', handleLoginSubmit);
        }

        const googleLoginBtn = document.getElementById('googleLoginBtn');
        if (googleLoginBtn) {
            googleLoginBtn.addEventListener('click', () => {
                showNotification('Función no implementada todavía.', 'info');
            });
        }
    }

    function handleLoginSubmit(event) {
        event.preventDefault();
        const form = event.target;
        form.submit();
    }

})();