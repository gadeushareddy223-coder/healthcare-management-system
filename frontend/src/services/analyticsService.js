import api from "./api";

export const getDashboardSummary = async () => {
    const response = await api.get("/analytics/summary");
    return response.data;
};

export const getAppointmentAnalytics = async () => {
    const response = await api.get("/analytics/appointments");
    return response.data;
};