import React from "react";
import styles from "./landingPage.styles.module.css";

const Main = () => {
  const handleLogout = () => {
    localStorage.removeItem("token");
    window.location.reload();
  };

  return (
    <div>
      <nav className={styles.navbar}>
        <div className={styles.navbar_left}>
          <h1>fakebook</h1>
          <p className={styles.navbar_link}>Home</p>
          <p className={styles.navbar_link}>Profile</p>
        </div>
        <div className={styles.navbar_right}>
          <button className={styles.white_btn} onClick={handleLogout}>
            Logout
          </button>
        </div>
      </nav>
    </div>
  );
};

export default Main;
