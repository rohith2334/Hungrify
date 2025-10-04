import React from "react";
import { Link, useNavigate } from "react-router-dom";
import "./navbar.styles.module.css";

const NavBar = () => {
  const navigate = useNavigate();

  const handleNavigation = (path) => {
    navigate(path);
  };

  return (
    <div className="navbar">
      <Link to="/">Home</Link>
      <Link to="/about">About</Link>
      <Link to="/contact">Contact</Link>
      <button onClick={() => handleNavigation("/example")}>Example</button>
    </div>
  );
};

export default NavBar;
