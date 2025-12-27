// Blood Bank Management System - Common JavaScript Functions

// Utility Functions
const Utils = {
    // Show alert message
    showAlert: function(message, type = 'success') {
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type}`;
        alertDiv.textContent = message;
        
        const container = document.querySelector('.container') || document.body;
        container.insertBefore(alertDiv, container.firstChild);
        
        setTimeout(() => {
            alertDiv.remove();
        }, 5000);
    },
    
    // Get URL parameter
    getUrlParameter: function(name) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(name);
    },
    
    // Format date
    formatDate: function(dateString) {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    },
    
    // Show loading spinner
    showLoading: function(elementId) {
        const element = document.getElementById(elementId);
        if (element) {
            element.innerHTML = '<div class="spinner"></div>';
        }
    },
    
    // Hide loading spinner
    hideLoading: function(elementId) {
        const element = document.getElementById(elementId);
        if (element) {
            element.innerHTML = '';
        }
    }
};

// API Functions
const API = {
    // Generic GET request
    get: async function(url) {
        try {
            const response = await fetch(url);
            return await response.json();
        } catch (error) {
            console.error('API Error:', error);
            throw error;
        }
    },
    
    // Generic POST request
    post: async function(url, data) {
        try {
            const formData = new FormData();
            for (const key in data) {
                formData.append(key, data[key]);
            }
            
            const response = await fetch(url, {
                method: 'POST',
                body: formData
            });
            
            return await response.json();
        } catch (error) {
            console.error('API Error:', error);
            throw error;
        }
    },
    
    // Get dashboard stats
    getDashboardStats: async function() {
        return await this.get('dashboard-stats');
    },
    
    // View donors
    viewDonors: async function(bloodGroup = '') {
        const url = bloodGroup 
            ? `view-donors?bloodGroup=${bloodGroup}`
            : 'view-donors';
        return await this.get(url);
    },
    
    // Add donor
    addDonor: async function(donorData) {
        return await this.post('add-donor', donorData);
    },
    
    // Delete donor
    deleteDonor: async function(donorId) {
        return await this.post('delete-donor', { donorId });
    },
    
    // Search donor
    searchDonor: async function(bloodGroup) {
        return await this.get(`search-donor?bloodGroup=${bloodGroup}`);
    },
    
    // View blood stock
    viewBloodStock: async function() {
        return await this.get('view-blood-stock');
    },
    
    // Add blood stock
    addBloodStock: async function(stockData) {
        return await this.post('add-blood-stock', stockData);
    },
    
    // Request blood
    requestBlood: async function(requestData) {
        return await this.post('request-blood', requestData);
    },
    
    // View blood issue history
    viewBloodIssue: async function() {
        return await this.get('view-blood-issue');
    }
};

// Form Validation
const Validator = {
    // Validate email
    isValidEmail: function(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    },
    
    // Validate phone number
    isValidPhone: function(phone) {
        const re = /^[0-9]{10}$/;
        return re.test(phone);
    },
    
    // Validate required field
    isRequired: function(value) {
        return value !== null && value !== undefined && value.trim() !== '';
    },
    
    // Validate number
    isValidNumber: function(value) {
        return !isNaN(value) && value > 0;
    }
};

// Check for error/success messages in URL
window.addEventListener('DOMContentLoaded', function() {
    const error = Utils.getUrlParameter('error');
    const success = Utils.getUrlParameter('success');
    
    if (error) {
        Utils.showAlert(decodeURIComponent(error), 'error');
    }
    
    if (success) {
        Utils.showAlert(decodeURIComponent(success), 'success');
    }
});

// Logout function
function logout() {
    if (confirm('Are you sure you want to logout?')) {
        window.location.href = 'logout';
    }
}
