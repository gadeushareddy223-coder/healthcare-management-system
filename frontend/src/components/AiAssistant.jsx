import { useState } from "react";
import { askAI } from "../services/aiService";

function AiAssistant() {
  const [question, setQuestion] = useState("");
  const [answer, setAnswer] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleAsk = async () => {
    if (!question.trim()) {
      setError("Please enter a question.");
      return;
    }

    setLoading(true);
    setError("");
    setAnswer("");

    try {
      const data = await askAI(question);

      setAnswer(data.answer);
    } catch (err) {
      console.error("AI Assistant Error:", err);

      if (err.response?.status === 401) {
        setError("Your session has expired. Please login again.");
      } else if (err.response?.status === 403) {
        setError("You are not authorized to use the AI Assistant.");
      } else {
        setError(
          "Unable to connect to the Healthcare AI Assistant."
        );
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      style={{
        maxWidth: "800px",
        margin: "40px auto",
        padding: "30px",
        borderRadius: "16px",
        backgroundColor: "#ffffff",
        boxShadow: "0 4px 20px rgba(0, 0, 0, 0.08)",
      }}
    >
      <h2
        style={{
          marginBottom: "8px",
          color: "#1f2937",
        }}
      >
        Healthcare AI Assistant
      </h2>

      <p
        style={{
          marginBottom: "24px",
          color: "#6b7280",
        }}
      >
        Ask general healthcare questions and get simple,
        educational information.
      </p>

      <textarea
        value={question}
        onChange={(e) => setQuestion(e.target.value)}
        placeholder="Example: What is diabetes?"
        rows="5"
        style={{
          width: "100%",
          padding: "14px",
          borderRadius: "10px",
          border: "1px solid #d1d5db",
          resize: "vertical",
          fontSize: "15px",
          boxSizing: "border-box",
          outline: "none",
        }}
      />

      <button
        onClick={handleAsk}
        disabled={loading}
        style={{
          marginTop: "15px",
          padding: "12px 24px",
          border: "none",
          borderRadius: "10px",
          backgroundColor: loading ? "#9ca3af" : "#2563eb",
          color: "#ffffff",
          fontSize: "15px",
          fontWeight: "600",
          cursor: loading ? "not-allowed" : "pointer",
        }}
      >
        {loading ? "Thinking..." : "Ask AI"}
      </button>

      {error && (
        <div
          style={{
            marginTop: "20px",
            padding: "14px",
            borderRadius: "10px",
            backgroundColor: "#fee2e2",
            color: "#b91c1c",
          }}
        >
          {error}
        </div>
      )}

      {answer && (
        <div
          style={{
            marginTop: "25px",
            padding: "20px",
            borderRadius: "12px",
            backgroundColor: "#f0fdf4",
            border: "1px solid #bbf7d0",
          }}
        >
          <h3
            style={{
              marginTop: 0,
              color: "#166534",
            }}
          >
            AI Response
          </h3>

          <p
            style={{
              whiteSpace: "pre-wrap",
              lineHeight: "1.7",
              color: "#374151",
            }}
          >
            {answer}
          </p>
        </div>
      )}

      <p
        style={{
          marginTop: "25px",
          fontSize: "12px",
          color: "#6b7280",
        }}
      >
        ⚠️ This AI assistant provides general educational
        information and does not replace professional medical advice.
      </p>
    </div>
  );
}

export default AiAssistant;