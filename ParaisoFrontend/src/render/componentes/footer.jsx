import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import { useState } from "react";
import { ArrowLeft, ArrowRight } from "lucide-react";
import { Facebook, Instagram, Twitter } from "lucide-react";

export default function Footer() {
  return (
    <footer className="bg-[#042B3C] text-white py-10 px-6 md:px-20 flex flex-col md:flex-row justify-between items-center md:items-start gap-10 relative">
      {/* Logo y nombre */}
      <div className="text-left">
        <h2 className="text-3xl text-[#F16717] font-semibold">Paraiso sv</h2>
      </div>

      {/* Navegación + redes */}
      <div className="text-center md:text-right">
        <div className="flex flex-wrap justify-center md:justify-end gap-6 text-sm mb-4">
          <a href="#" className="hover:underline">
            Adopta
          </a>
          <a href="#" className="hover:underline">
            Comprar rifa
          </a>
          <a href="#" className="hover:underline">
            Proyectos
          </a>
          <a href="#" className="hover:underline">
            Alcancía
          </a>
          <a href="#" className="hover:underline">
            Comprar Productos
          </a>
        </div>

        <p className="mb-2">Paraiso sv</p>

        <div className="flex justify-center md:justify-end gap-4">
          <a href="#" className="bg-white text-[#042B3C] p-2 rounded-full">
            <Facebook size={20} />
          </a>
          <a href="#" className="bg-white text-[#042B3C] p-2 rounded-full">
            <Instagram size={20} />
          </a>
          <a href="#" className="bg-white text-[#042B3C] p-2 rounded-full">
            <Twitter size={20} />
          </a>
        </div>
      </div>
    </footer>
  );
}
