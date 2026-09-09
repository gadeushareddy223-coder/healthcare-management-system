import api from "./api";

export const getDoctors = async () => {
  const token = localStorage.getItem("token");

  const response = await api.get("/doctors", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  return response.data;
};