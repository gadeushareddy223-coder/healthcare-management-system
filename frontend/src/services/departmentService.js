import api from "./api";

export const getDepartments = async () => {
  const token = localStorage.getItem("token");

  const response = await api.get("/departments", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  return response.data;
};