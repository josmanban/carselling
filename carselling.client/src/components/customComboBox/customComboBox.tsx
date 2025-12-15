import { get } from "http";
import { useState } from "react";
import { TextField, Button, InputLabel, Select, MenuItem, FormControl, SelectChangeEvent, Menu } from "@mui/material";


function CustomComboBox<T>(props: {
    options: T[];
    getOptionLabel: (option: T) => string;
    getValue: (option: T) => string | number;
    //React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement> | SelectChangeEvent<string>
    handleChange: (e: any) => void;
    label: string;
    selectedValue: string | number | undefined;
    hasEmptyOption?: boolean;
    name:string,
    id:string,
}) {
    const { 
        options, 
        getOptionLabel, 
        handleChange, 
        getValue, 
        label, 
        selectedValue, 
        hasEmptyOption,
        name,
        id
    } = props;

    return (
        <FormControl
            fullWidth
            margin="normal"
        >
            <InputLabel id="demo-simple-select-label">{label}</InputLabel>
            <Select
                id={id}
                name={name}                
                value={selectedValue}            
                onChange={handleChange}
                >
                {hasEmptyOption && (<MenuItem value="">Seleccione...</MenuItem>)}
                {options.map((option, index) => (
                    <MenuItem
                        key={index}
                        value={getValue(option)}                        
                        >                        
                        {getOptionLabel(option)}
                    </MenuItem>
                ))}
            </Select>
        </FormControl>
    );
}

export default CustomComboBox;