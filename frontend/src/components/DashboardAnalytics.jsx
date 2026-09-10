import { useEffect, useState } from "react";
import {
    PieChart,
    Pie,
    Cell,
    Tooltip,
    Legend,
    ResponsiveContainer
} from "recharts";

import {
    getDashboardSummary,
    getAppointmentAnalytics
} from "../services/analyticsService";

function DashboardAnalytics() {

    const [summary, setSummary] = useState(null);
    const [appointments, setAppointments] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {

        const loadAnalytics = async () => {

            try {

                setLoading(true);
                setError("");

                const summaryData =
                    await getDashboardSummary();

                const appointmentData =
                    await getAppointmentAnalytics();

                setSummary(summaryData);
                setAppointments(appointmentData);

            } catch (err) {

                console.error(
                    "Failed to load dashboard analytics:",
                    err
                );

                setError(
                    "Unable to load dashboard analytics."
                );

            } finally {

                setLoading(false);

            }
        };

        loadAnalytics();

    }, []);

    if (loading) {
        return (
            <div className="analytics-message">
                Loading dashboard analytics...
            </div>
        );
    }

    if (error) {
        return (
            <div className="analytics-error">
                {error}
            </div>
        );
    }

    const appointmentChartData = [
        {
            name: "Scheduled",
            value: appointments?.scheduled || 0
        },
        {
            name: "Completed",
            value: appointments?.completed || 0
        },
        {
            name: "Cancelled",
            value: appointments?.cancelled || 0
        },
        {
            name: "Pending",
            value: appointments?.pending || 0
        }
    ];

    return (
        <section className="dashboard-analytics">

            <div className="analytics-header">

                <div>
                    <p className="analytics-label">
                        HEALTHCARE OVERVIEW
                    </p>

                    <h2>
                        Dashboard Analytics
                    </h2>

                    <p>
                        Monitor your healthcare system
                        activity at a glance.
                    </p>
                </div>

            </div>

            <div className="analytics-cards">

                <div className="analytics-card">
                    <span>👨‍⚕️</span>

                    <div>
                        <p>Total Doctors</p>
                        <h3>{summary?.totalDoctors || 0}</h3>
                    </div>
                </div>

                <div className="analytics-card">
                    <span>🧑‍🤝‍🧑</span>

                    <div>
                        <p>Total Patients</p>
                        <h3>{summary?.totalPatients || 0}</h3>
                    </div>
                </div>

                <div className="analytics-card">
                    <span>🏥</span>

                    <div>
                        <p>Total Departments</p>
                        <h3>{summary?.totalDepartments || 0}</h3>
                    </div>
                </div>

                <div className="analytics-card">
                    <span>📅</span>

                    <div>
                        <p>Total Appointments</p>
                        <h3>{summary?.totalAppointments || 0}</h3>
                    </div>
                </div>

            </div>

            <div className="analytics-chart-card">

                <div className="chart-header">

                    <div>
                        <p className="analytics-label">
                            APPOINTMENTS
                        </p>

                        <h3>
                            Appointment Status
                        </h3>
                    </div>

                    <div className="appointment-total">
                        Total: {appointments?.total || 0}
                    </div>

                </div>

                <div className="appointment-chart">

                    <ResponsiveContainer
                        width="100%"
                        height={320}
                    >

                        <PieChart>

                            <Pie
                                data={appointmentChartData}
                                dataKey="value"
                                nameKey="name"
                                cx="50%"
                                cy="50%"
                                outerRadius={105}
                                innerRadius={55}
                                paddingAngle={3}
                                label
                            >

                                {appointmentChartData.map(
                                    (entry, index) => (
                                        <Cell
                                            key={`cell-${index}`}
                                        />
                                    )
                                )}

                            </Pie>

                            <Tooltip />

                            <Legend />

                        </PieChart>

                    </ResponsiveContainer>

                </div>

            </div>

        </section>
    );
}

export default DashboardAnalytics;