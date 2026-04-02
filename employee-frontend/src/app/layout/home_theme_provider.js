import { createContext, useState } from "react";

export const ThemeContext = createContext();

const HomeThemeProvider = ({ children }) => {

    const [theme, setTheme] = useState({
        background: "#fff",
        text: "#000"
    });

    return (
        <ThemeContext.Provider value={{ theme, setTheme }}>
            {children}
        </ThemeContext.Provider>
    );
}

export default HomeThemeProvider;