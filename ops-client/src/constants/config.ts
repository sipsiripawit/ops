// API Endpoints
export const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:3000/api';

// Auth Configuration
export const AUTH_CONFIG = {
    ACCESS_TOKEN_KEY: 'accessToken',
    REFRESH_TOKEN_KEY: 'refreshToken',
    TOKEN_EXPIRY_BUFFER: 60 * 1000, // 1 minute buffer before token expiry
};

// Application Version
export const APP_VERSION = '0.0.0';
export const LAST_UPDATED = '2568-01-31 09:38';

// Theme Configuration
export const THEME = {
    PRIMARY: '#050A30', // Dark blue
    SECONDARY: '#0A1857', // Slightly lighter blue
    ACCENT: '#E50012', // Red for YIP logo
    SUCCESS: '#10B981', // Green for success messages
    WARNING: '#F59E0B', // Yellow for warnings
    ERROR: '#EF4444', // Red for errors
};