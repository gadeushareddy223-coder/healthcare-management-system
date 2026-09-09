import { useEffect, useState } from "react";
import api from "./services/api";
import "./App.css";

function App() {
  const [username, setUsername] = useState("");
  const [role, setRole] = useState("");
  const [token, setToken] = useState("");
  const [departments, setDepartments] = useState([]);

  const [loginUsername, setLoginUsername] = useState("");
  const [loginPassword, setLoginPassword] = useState("");

  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const savedToken = localStorage.getItem("token");
    const savedUsername = localStorage.getItem("username");
    const savedRole = localStorage.getItem("role");

    if (savedToken) {
      setToken(savedToken);
      setUsername(savedUsername || "");
      setRole(savedRole || "");

      loadDepartments(savedToken);
    }
  }, []);

  const loadDepartments = async (authToken) => {
    try {
      const response = await api.get("/departments", {
        headers: {
          Authorization: `Bearer ${authToken}`,
        },
      });

      setDepartments(response.data);
    } catch (err) {
      console.error("Failed to load departments:", err);
      setError("Unable to load departments.");
    }
  };

  const handleLogin = async (event) => {
    event.preventDefault();

    setMessage("");
    setError("");
    setLoading(true);

    try {
      const response = await api.post("/auth/login", {
        username: loginUsername,
        password: loginPassword,
      });

      const receivedToken = response.data.token;
      const receivedUsername = response.data.username;
      const receivedRole = response.data.role;

      localStorage.setItem("token", receivedToken);
      localStorage.setItem("username", receivedUsername);
      localStorage.setItem("role", receivedRole);

      setToken(receivedToken);
      setUsername(receivedUsername);
      setRole(receivedRole);

      setMessage("Login successful!");

      setLoginPassword("");

      await loadDepartments(receivedToken);
    } catch (err) {
      console.error("Login error:", err);

      setError(
        "Login failed. Please check your username and password."
      );
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    localStorage.removeItem("role");

    setToken("");
    setUsername("");
    setRole("");
    setDepartments([]);

    setMessage("You have been logged out.");
    setError("");
  };

  return (
    <div className="app">

      {/* Background decoration */}
      <div className="background-circle circle-one"></div>
      <div className="background-circle circle-two"></div>

      <div className="page-container">

        {/* Header */}
        <header className="header">

          <div className="brand">

            <div className="brand-icon">
              +
            </div>

            <div>
              <h1>Healthcare Management</h1>

              <p>
                Smart Healthcare
                <span>•</span>
                Secure Management
                <span>•</span>
                Better Care
              </p>
            </div>

          </div>

        </header>

        {/* Login Section */}
        {!token ? (

          <section className="login-section">

            <div className="login-card">

              <div className="login-icon">
                +
              </div>

              <h2>Welcome Back</h2>

              <p className="login-subtitle">
                Sign in to access your healthcare dashboard
              </p>

              <form onSubmit={handleLogin}>

                <div className="form-group">

                  <label>Username</label>

                  <input
                    type="text"
                    placeholder="Enter your username"
                    value={loginUsername}
                    onChange={(event) =>
                      setLoginUsername(event.target.value)
                    }
                    required
                  />

                </div>

                <div className="form-group">

                  <label>Password</label>

                  <input
                    type="password"
                    placeholder="Enter your password"
                    value={loginPassword}
                    onChange={(event) =>
                      setLoginPassword(event.target.value)
                    }
                    required
                  />

                </div>

                <button
                  className="login-button"
                  type="submit"
                  disabled={loading}
                >
                  {loading ? "Signing in..." : "Sign In"}
                </button>

              </form>

              {error && (
                <div className="error-message">
                  {error}
                </div>
              )}

            </div>

          </section>

        ) : (

          <>

            {/* User Card */}
            <section className="user-card">

              <div className="user-info">

                <div className="avatar">
                  {username.charAt(0).toUpperCase()}
                </div>

                <div>

                  <span className="welcome-text">
                    WELCOME BACK
                  </span>

                  <div className="user-name-row">

                    <h3>{username}</h3>

                    <span className="role-badge">
                      {role}
                    </span>

                  </div>

                </div>

              </div>

              <button
                className="logout-button"
                onClick={handleLogout}
              >
                Logout
              </button>

            </section>

            {/* Success Message */}
            {message && (
              <div className="success-message">
                <span className="success-icon">✓</span>
                {message}
              </div>
            )}

            {/* Department Section */}
            <section className="department-section">

              <div className="section-heading">

                <div className="section-icon">
                  +
                </div>

                <div>

                  <h2>
                    Healthcare Departments
                  </h2>

                  <p>
                    Manage and access available medical departments
                  </p>

                </div>

              </div>

              {departments.length === 0 ? (

                <div className="empty-state">

                  <div className="empty-icon">
                    +
                  </div>

                  <h3>
                    No departments available
                  </h3>

                  <p>
                    There are currently no departments in the system.
                  </p>

                </div>

              ) : (

                <div className="department-grid">

                  {departments.map((department, index) => (

                    <div
                      className="department-card"
                      key={department.id}
                    >

                      <div className="card-top">

                        <div className="department-icon">
                          +
                        </div>

                        <span className="department-number">
                          #{String(index + 1).padStart(2, "0")}
                        </span>

                      </div>

                      <div className="department-content">

                        <h3>
                          {department.name}
                        </h3>

                        <p>
                          {department.description ||
                            "Medical healthcare department"}
                        </p>

                      </div>

                      <div className="card-footer">

                        <span>
                          MEDICAL DEPARTMENT
                        </span>

                        <span className="arrow">
                          →
                        </span>

                      </div>

                    </div>

                  ))}

                </div>

              )}

            </section>

          </>

        )}

        {/* Footer */}
        <footer className="footer">

          <div className="footer-line"></div>

          <h3>
            Healthcare Management System
          </h3>

          <p>
            Secure
            <span>•</span>
            Reliable
            <span>•</span>
            Patient-Centered
          </p>

          <small>
            © 2026 Healthcare Management System
          </small>

        </footer>

      </div>

    </div>
  );
}

export default App;