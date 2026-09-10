import api from "./api";

const getAuthConfig = () => {
  const token = localStorage.getItem("token");

  return {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };
};

const getNotifications = async () => {
  const response = await api.get(
    "/notifications",
    getAuthConfig()
  );

  return response.data;
};

const getUnreadNotifications = async () => {
  const response = await api.get(
    "/notifications/unread",
    getAuthConfig()
  );

  return response.data;
};

const markNotificationAsRead = async (notificationId) => {
  const response = await api.put(
    `/notifications/${notificationId}/read`,
    {},
    getAuthConfig()
  );

  return response.data;
};

const deleteNotification = async (notificationId) => {
  await api.delete(
    `/notifications/${notificationId}`,
    getAuthConfig()
  );
};

const createNotification = async (
  userId,
  message,
  type
) => {
  const response = await api.post(
    "/notifications",
    {
      userId,
      message,
      type,
    },
    getAuthConfig()
  );

  return response.data;
};

export {
  getNotifications,
  getUnreadNotifications,
  markNotificationAsRead,
  deleteNotification,
  createNotification,
};